package com.example

import com.example.data.model.PrizeRarity
import com.example.data.model.PrizeType
import com.example.data.model.UpgradeType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun prizeRarity_valuesAreConsistent() {
        assertTrue(PrizeRarity.COMMON.baseValueCoins < PrizeRarity.RARE.baseValueCoins)
        assertTrue(PrizeRarity.RARE.baseValueCoins < PrizeRarity.EPIC.baseValueCoins)
        assertTrue(PrizeRarity.EPIC.baseValueCoins < PrizeRarity.LEGENDARY.baseValueCoins)
        assertTrue(PrizeRarity.LEGENDARY.baseValueCoins < PrizeRarity.JACKPOT.baseValueCoins)
    }

    @Test
    fun upgradeTypes_haveValidMaxLevelsAndCosts() {
        for (type in UpgradeType.values()) {
            assertTrue(type.maxLevel > 0)
            assertTrue(type.baseCostCoins > 0)
            assertTrue(type.costMultiplier > 1.0f)
        }
    }

    @Test
    fun prizeCatalog_hasAllRequiredPrizes() {
        assertNotNull(PrizeType.valueOf("TEDDY_BEAR"))
        assertNotNull(PrizeType.valueOf("RAINBOW_UNICORN"))
        assertEquals(9, PrizeType.values().size)
    }
}
