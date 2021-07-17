package com.percivalruiz.kumu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.percivalruiz.kumu.data.ITunesItem
import com.percivalruiz.kumu.data.RemoteKey
import com.percivalruiz.kumu.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ITunesItemDAOTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        db.close()
    }

    @Test
    fun insertAllAndGetAllItem() = runBlockingTest {
        val items = listOf(
            createItem(0, "Loki", 0f),
            createItem(1, "Sylvie", 1f),
            createItem(2, "Kang", 2f)
        )
        db.iTunesItemDAO().insertAll(*items.toTypedArray())
        val userDb = async {
            db.iTunesItemDAO().getAll()
        }

        userDb.await().let {
            Assert.assertNotNull(it)
        }
    }

    private fun createItem(id: Long, name: String, price: Float) = ITunesItem(
        id = id,
        trackName = name,
        artworkUrl100 = "artworkUrl100 #$id",
        trackPrice = price,
        primaryGenreName = "primaryGenreName #$id",
        currency = "currency #$id",
        longDescription = "description #$id"
    )
}
