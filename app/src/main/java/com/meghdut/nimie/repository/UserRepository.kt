package com.meghdut.nimie.repository

import android.util.Base64
import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.LocalUser
import com.meghdut.nimie.model.RegisterUser
import com.meghdut.nimie.network.NimieApi
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import kotlin.random.Random

class UserRepository(val db: NimieDb) {

    val nimieApi = NimieApi.instance
    val userDao = db.userDao()


    fun newUser(): LocalUser {
        val (publicKey, privateKey) = generateKeyPair()
        val name =
            nameList[Random.nextInt(nameList.size)] + " " + nameList[Random.nextInt(nameList.size)]
        val avatar = avatar(name)
        val execute = nimieApi.registerUser(RegisterUser(publicKey)).execute()
        if (execute.isSuccessful) {
            val userCreated = execute.body() ?: throw Exception("Unable to connect to server!")
            val userLocal = LocalUser(userCreated.userId, publicKey, privateKey, name, avatar, 1)
            userDao.clearData()
            userDao.insert(userLocal)
            return userLocal
        } else throw Exception(execute.errorBody()?.string())
    }

    fun generateKeyPair(): Pair<String, String> {
        val generator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val pair: KeyPair = generator.generateKeyPair()
        val privateKey: PrivateKey = pair.private
//        println("The privatekey:  " + privateKey.encoded.toBase64())
        val publicKey: PublicKey = pair.public
//        println("The public key : " + publicKey.encoded.toBase64())
        return publicKey.encoded.toBase64() to privateKey.encoded.toBase64()
    }

    fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)

    fun avatar(name: String) = "https://avatars.dicebear.com/api/avataaars/${name.hashCode()}.svg"


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
}