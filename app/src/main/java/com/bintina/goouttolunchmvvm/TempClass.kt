package com.bintina.goouttolunchmvvm

class TempClass {
    /* Strings
    <resources>
    <string name="app_name">Go Out To Lunch MVVM</string>
</resources>
     */

    /* themes.xml
    resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.GoOutToLunchMVVM" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your light theme here. -->
        <!-- <item name="colorPrimary">@color/my_light_primary</item> -->
    </style>

    <style name="Theme.GoOutToLunchMVVM" parent="Base.Theme.GoOutToLunchMVVM" />
</resources>
     */

    /*themes.xml(night)
    <resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.GoOutToLunchMVVM" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your dark theme here. -->
        <!-- <item name="colorPrimary">@color/my_dark_primary</item> -->
    </style>
</resources>
     */

/*    testOptions {
        unitTests {
            // Disable StrictMode for instrumented tests
            all {
                Instrumented.systemProperty(
                    "org.junit.runners.JUnitCore.defaultResourceName",
                    "JUnit3"
                )
            }
        }
    }*/
    /* @RequiresApi(Build.VERSION_CODES.P)
    fun getSha() {
        val packageName = "com.bintina.myfirebaseapplication"

        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)

            for (signature in info.signingInfo.signingCertificateHistory) {
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                md.update(signature.toByteArray())

                // Convert the byte array to hexadecimal representation
                val sha256Hex = bytesToHex(md.digest())

                Log.d("SHA", sha256Hex)

                val keyHash = String(Base64.encode(md.digest(), Base64.NO_WRAP))
                Log.e("hash key", keyHash)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("name not found", e.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v shr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }*/
}