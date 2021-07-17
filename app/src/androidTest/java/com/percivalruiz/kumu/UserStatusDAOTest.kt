package com.percivalruiz.kumu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.percivalruiz.kumu.data.UserStatus
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
class UserStatusDAOTest {

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
    fun insertAndPeekStatus() = runBlockingTest {
        val status = UserStatus(id = 0, lastSearch = "Loki", lastVisit = 1)
        db.userStatusDAO().insert(status)
        val keyDb = async {
            db.userStatusDAO().peek()
        }

        keyDb.await().let {
            Assert.assertEquals(status.id, it?.id)
            Assert.assertEquals(status.lastSearch, it?.lastSearch)
            Assert.assertEquals(status.lastVisit, it?.lastVisit)
        }
    }

    @Test
    fun deleteStatus() = runBlockingTest {
        val status = UserStatus(id = 0, lastSearch = "Loki", lastVisit = 1)
        db.userStatusDAO().insert(status)
        var keyDb = async {
            db.userStatusDAO().peek()
        }

        keyDb.await().let {
            Assert.assertEquals(status.id, it?.id)
            Assert.assertEquals(status.lastSearch, it?.lastSearch)
            Assert.assertEquals(status.lastVisit, it?.lastVisit)
        }

        db.userStatusDAO().nuke()
        keyDb = async {
            db.userStatusDAO().peek()
        }

        Assert.assertEquals(null, keyDb.await())
    }
}
