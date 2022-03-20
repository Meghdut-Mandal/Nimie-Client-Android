package com.meghdut.nimie.ui.util

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.random.Random


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
