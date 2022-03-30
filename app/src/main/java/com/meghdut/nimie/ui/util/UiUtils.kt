package com.meghdut.nimie.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.meghdut.nimie.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.random.Random



data class OnBoardingItem(val titleSrcId: Int, val descriptionSrcId: Int, val imageSrcId: Int)

object SplashUtils{
        fun getOnBoardingItems(): List<OnBoardingItem> = listOf(firstIntro, secondIntro, thirdIntro)

    private val firstIntro: OnBoardingItem = OnBoardingItem(
        R.string.first_intro_title,
        R.string.first_intro_description,
        R.drawable.ic_intro_first
    )

    private val secondIntro: OnBoardingItem = OnBoardingItem(
        R.string.second_intro_title,
        R.string.second_intro_description,
        R.drawable.ic_intro_second
    )

    private val thirdIntro: OnBoardingItem = OnBoardingItem(
        R.string.third_intro_title,
        R.string.third_intro_description,
        R.drawable.ic_intro_third
    )
}




fun Activity.navigateTo(targetActivity: Class<*>) {
    val intent = Intent(this, targetActivity)
    startActivity(intent)
    finishAffinity()
}

fun snackBar(
    view: View,
    message: String
) {
    Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG)
        .show()
}

fun AndroidViewModel.ioTask(func: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        func()
    }
}


fun Context.getDisplayableDateOfGivenTimeStamp(
    timeStamp: Long,
    timeOnly: Boolean
): String? {
    val thisDay: Boolean
    val yesterday: Boolean
    val date = Date(timeStamp)

    //String format of date only {03/04/2020}
    val dateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

    //String format of date only {12:53 AM}
    val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
    val cal = Calendar.getInstance()
    cal.time = date
    val rightNow = Calendar.getInstance()
    thisDay = rightNow[Calendar.DAY_OF_WEEK] == cal[Calendar.DAY_OF_WEEK]
    yesterday = rightNow[Calendar.DAY_OF_WEEK] - cal[Calendar.DAY_OF_WEEK] == 1
    if (timeOnly || thisDay) {
        return timeString
    }
    return if (yesterday) {
        getString(R.string.yesterday)
    } else dateString

    // if more than yesterday
}

fun hasPermissionInManifest(
    activity: Activity?,
    requestCode: Int,
    permissionName: String
): Boolean {
    if (ContextCompat.checkSelfPermission(
            activity!!,
            permissionName
        )
        != PackageManager.PERMISSION_GRANTED
    ) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(
            activity, arrayOf(permissionName),
            requestCode
        )
    } else {
        return true
    }
    return false
}


fun avatar(name: String) =
    "https://avatars.dicebear.com/api/avataaars/${name.hashCode().absoluteValue}.png"

val nameList = """Grn
Bana
Gor
Gof
Vodancaggll
Det
Examia
Grl
Colron
Dwl
Gus
Mad
Crarskeona
Meo
Drtlatis
Zopt
Stth
Kluzlbaga
Shikenomee
Sty
Helinner
Vadsllpe
Ark
Esc
Gling
Cactipoonax
Vit
Pssprper
Frphok
Golaku
Liburowedsky
Toshaus
Drurablloion
Gritow
Vabampospll
Kinouzilat
Ratytolgo
Blik
Gor
Exuly
Baranean
Sadrd
Drsh
Lakadeleaste
Ziaroratostt
Ais
Lug
Sley
Muspe
Azmar
Vopoon
Schana
Tow
Shum
Geariek
Flilinota
Lus
Skigobroton
Tenky
Makumpurory
Aralgen
Migorawseta
Abor
Con
Spinglt
Wite
Hoc
Xaltie
Brant
Hetmorustapt
Lauraquros
Ablt
Depipifff
Lor
Hoecidee
Rellecctige
Linadss
Deliny
Dela
Giter
Jome
Mazachyel
Sprie
Wumelede
Tiow
Scrum
Mel
Gauroslew
Sumey
Bevatirctlor
Kyng
Acufapikia
Dubartad
Skulis
Phiffff
Meranx
Dinelagggck
Tygoramish
Yashabi
Hamena
Lotinct
Weror
Mueltur
Voin
Paritty
Lazmpa
Fia
Nus
Lowlikum
Squbed
Goroga
Scecumbraf
Zurcadse
Cagitt
Noncank
Hindo
Pirchell
Lut
Melanetmigat
Sntrtcaton
Pordeo
Brass
Panndrodipow
Med
Aupieyebcon
Stolons
Nip
Lite
Poworic
Pos
Mamos
Cuibet
Dumagow
Aritheetin
Penchano
Lint
Lus
Rer
Skuni
Zowpodobbse
Trage
Pitosshiste
Camedrn
Tryu
Arrte
Covadieado
Bew
Deekuat
Kaswaspleana
Herog
Stoom
Race
Schag
Ginzoawing
Onge
Stytle
Gyrayedunus
Navis
Smset
Palic
Kawin
Grin
Whanflasta
Larl
Ligolleer
Mailtin
Tonflew
Mile
Mauflgaude
Dildin
Depana
Saly
Dureno
Gatunckas
Sck
Nile
Meerobucoss
Swhebly
Misde
Vancarula
Fel
Pye
Roowlill
Litt
Xesk
Mumeewkan
Tarow
Tritt
Sth
Vel
Exem
Pinevazutoki
Reeturta
Garm
Samew
Gomagoric
Card
Kin
Drratata
Crot
Ton
Denme
Tew
Mateliee
Gate
Hin
Doddugne
Wolgan
Rhi
Pactus
Cacacle
Shuix
Furele
Groof
Garid
Pooroh
Urint
Weathoin
Jio
Ver
Tybi
Pat
Exeit
Brodounanai
Vog
Yvas
Phamspung
Drir
Toriman
Lim
Wacink
Sng
Ell
Chish
Pang
Mar
Las
Checuga
Revet
Kat
Vilbrla
Gaveoauraps
Mirictaro
Drdirtic
Nosh
Sll
Gee
Raganepll
Ocon
Sntt
Shtra
Kriqum
Witolle
Blgegorure
Jyowstogana
Biruborty
Shu
Ker
Wyneoufflie
Rholarener
Bailezlf
Cund
Chen
Dur
Dela
Ameng
Visellce
Aree
Tiver
Skare
Rowk
Dare
Banith
Goolus
Crangush
Eshisllfkic
Ema
Exilcus
Ant
Scarmic
Scton
Poldurickeer
Shavellerus
Eedyhyu
Krooy
Cobre
Mean
Ning
Minea
Fride""".split("\n")


val randomName: String
    get() =
        """${nameList[Random.nextInt(nameList.size)]} ${nameList[Random.nextInt(nameList.size)]}"""
