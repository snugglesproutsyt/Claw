package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class ArcadeSoundManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private var toneGen: ToneGenerator? = null

    init {
        try {
            toneGen = ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 70)
        } catch (_: Exception) {
            toneGen = null
        }
    }

    // Play arcade frequency wave
    private fun playSynthTone(frequencies: List<Pair<Int, Int>>) { // frequency Hz, duration Ms
        scope.launch {
            try {
                val sampleRate = 22050
                var totalSamples = 0
                for ((_, dur) in frequencies) {
                    totalSamples += (sampleRate * (dur / 1000.0)).toInt()
                }
                val buffer = ShortArray(totalSamples)
                var offset = 0

                for ((freq, dur) in frequencies) {
                    val count = (sampleRate * (dur / 1000.0)).toInt()
                    for (i in 0 until count) {
                        val angle = 2.0 * Math.PI * i * freq / sampleRate
                        // Soft envelope at edges to prevent clicking
                        val envelope = when {
                            i < 100 -> i / 100.0
                            i > count - 100 -> (count - i) / 100.0
                            else -> 1.0
                        }
                        val sample = (sin(angle) * 26000 * envelope).toInt().toShort()
                        if (offset + i < buffer.size) {
                            buffer[offset + i] = sample
                        }
                    }
                    offset += count
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()
                // Auto release after sound
                scope.launch {
                    val totalDuration = frequencies.sumOf { it.second }
                    kotlinx.coroutines.delay(totalDuration.toLong() + 100)
                    track.release()
                }
            } catch (_: Exception) {
                // Fallback to ToneGenerator if AudioTrack unavailable
                try {
                    toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
                } catch (_: Exception) {}
            }
        }
    }

    fun playCoinInsert() {
        vibrate(30)
        playSynthTone(listOf(987 to 60, 1318 to 120)) // B5 -> E6 classic coin sound
    }

    fun playClawMove() {
        vibrate(10)
        playSynthTone(listOf(320 to 25))
    }

    fun playDropInitiated() {
        vibrate(40)
        playSynthTone(listOf(523 to 80, 440 to 80, 349 to 120))
    }

    fun playClawGrab() {
        vibrateHeavy()
        playSynthTone(listOf(220 to 60, 180 to 80, 150 to 100))
    }

    fun playPrizeWon() {
        vibratePattern(longArrayOf(0, 80, 50, 120, 50, 200))
        playSynthTone(
            listOf(
                523 to 80,  // C5
                659 to 80,  // E5
                784 to 80,  // G5
                1046 to 180 // C6
            )
        )
    }

    fun playSlipMiss() {
        vibrate(50)
        playSynthTone(listOf(392 to 100, 349 to 100, 311 to 200))
    }

    fun playUpgradePurchase() {
        vibrate(35)
        playSynthTone(listOf(659 to 60, 880 to 80, 1174 to 140))
    }

    fun playButtonClick() {
        vibrate(15)
        playSynthTone(listOf(750 to 20))
    }

    private fun vibrate(durationMs: Long) {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            durationMs,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {}
    }

    private fun vibrateHeavy() {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(100)
                }
            }
        } catch (_: Exception) {}
    }

    private fun vibratePattern(timings: LongArray) {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(timings, -1)
                }
            }
        } catch (_: Exception) {}
    }
}
