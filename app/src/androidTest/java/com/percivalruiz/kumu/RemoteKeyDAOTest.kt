package com.percivalruiz.kumu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class RemoteKeyDAOTest {

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
    fun insertAndPeekKey() = runBlockingTest {
        val key = RemoteKey(nextPageKey = 1)
        db.remoteKeyDAO().insert(key)
        val keyDb = async {
            db.remoteKeyDAO().peek()
        }

        keyDb.await().let {
            Assert.assertEquals(key.nextPageKey, it?.nextPageKey)
            Assert.assertEquals(key.id, it?.id)
        }
    }

    @Test
    fun deleteKey() = runBlockingTest {
        val key = RemoteKey(nextPageKey = 1)
        db.remoteKeyDAO().insert(key)
        var keyDb = async {
            db.remoteKeyDAO().peek()
        }

        keyDb.await().let {
            Assert.assertEquals(key.nextPageKey, it?.nextPageKey)
            Assert.assertEquals(key.id, it?.id)
        }

        db.remoteKeyDAO().nuke()
        keyDb = async {
            db.remoteKeyDAO().peek()
        }

        Assert.assertEquals(null, keyDb.await())
    }
}
