package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.GameRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Claw Master", appName)
    }

    @Test
    fun `repository initializes with starting balance and standard claw`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = GameRepository(context)

        assertTrue(repo.coins.value >= 200)
        assertEquals("claw_classic", repo.equippedClawId.value)
        assertNotNull(repo.getEquippedClaw())
        assertEquals(7, repo.clawCatalog.size)
    }
}
