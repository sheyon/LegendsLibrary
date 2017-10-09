//package com.sheyon.fivecats.legendslibrary;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.sheyon.fivecats.legendslibrary.data.LegendsPreferences;
//
//import java.util.ArrayList;
//
//public class LocationActivity extends AppCompatActivity {
//
//    private String titleString;
//    private ArrayList<LegendsLocation> locations;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_location);
//
//        Toolbar toolbar = findViewById(R.id.locationActivity_toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        titleString = getIntent().getStringExtra("loreTitle");
//        if (titleString == null) {
//            titleString = LegendsPreferences.getInstance(this).getLoreTitle();
//        }
//
//        TextView loreTitleTextView = findViewById(R.id.locationActivity_title);
//        loreTitleTextView.setText(titleString);
//
//        fillArray();
//
//        ListView listView = findViewById(R.id.location_listView);
//        LegendsLocationAdapter adapter = new LegendsLocationAdapter(this, 0, locations);
//        listView.setAdapter(adapter);
//    }
//
//    private void fillArray() {
//        locations = new ArrayList<LegendsLocation>();
//
//        switch (titleString) {
//            case "1712 Fire":
//            case "L'incendie de 1712":
//            case "Feuer von 1712":
//                locations.add(new LegendsLocation(1, "Kingsmouth", 250, 510, "Behind the crypt, behind the Kingsmouth church"));
//                locations.add(new LegendsLocation(2, "Kingsmouth", 470, 525, "On the second floor of the Fire Department, next to Fire Chief Soule"));
//                locations.add(new LegendsLocation(3, "Kingsmouth", 175, 615, "In the mass grave"));
//                locations.add(new LegendsLocation(4, "Kingsmouth", 165, 565, "In the mass grave"));
//                locations.add(new LegendsLocation(5, "Kingsmouth", 360, 350, "On the second floor of the Town Hall"));
//                locations.add(new LegendsLocation(6, "Kingsmouth", 385, 260, "On the beach access"));
//                locations.add(new LegendsLocation(7, "Kingsmouth", 340, 455, "Behind the Kingsmouth Church sign"));
//                locations.add(new LegendsLocation(8, "Kingsmouth", 40, 745, "On the ledge above the Blue Mountain access tunnel"));
//                locations.add(new LegendsLocation(9, "Kingsmouth", 255, 785, "Under the Bridge"));
//                locations.add(new LegendsLocation(10, "Kingsmouth", 50, 650, "South of the Blue Mountain access tunnel, against the cliff-face"));
//                break;
//
//            case "Lilith":
//                locations.add(new LegendsLocation(1, "Kingsmouth", 250, 510, "Notes"));
//                break;
//
//            case "Opening Night":
//            case "La première":
//            case "Premierenabend":
//                locations.add(new LegendsLocation(1, "Kingsmouth", 250, 510, "Notes"));
//                break;
//
//            case "Agartha":
//            case "L'Agartha":
//                locations.add(new LegendsLocation(1, "Kingsmouth", 250, 510, "Notes"));
//                break;
//
//            case "Anastasia's Wagon":
//            case "La roulotte d'Anastasia":
//            case "Anastasias Wagen":
//                locations.add(new LegendsLocation(1, "Kingsmouth", 250, 510, "Notes"));
//                break;
//
//            case "Breach of the First Wall":
//            case "La brèche du premier mur":
//            case "Fall der ersten Mauer":
//                break;
//
//            case "Council of Venice":
//            case "Le Conseil de Venise":
//            case "Rat von Venedig":
//                break;
//
//            case "The Drăculești":
//            case "Les Drăculești":
//            case "Drăculești":
//                break;
//
//            case "Dragon":
//            case "Drachen":
//                break;
//
//            case "Fear Nothing Foundation":
//            case "La Fondation Fear Nothing":
//                break;
//
//            case "Gaia Engines":
//            case "Les machines de Gaia":
//            case "Gaia-Motoren":
//                break;
//
//            case "Ginpachi Park":
//            case "Parc Ginpachi":
//            case "Ginpachi-Park":
//                break;
//
//            case "Illuminati":
//            case "Illuminaten":
//                break;
//
//            case "Innsmouth Academy":
//            case "Académie d'Innsmouth":
//                break;
//
//            case "Jigoku no Yu Bathhouse":
//            case "Bains publics Jigoku no Yu":
//            case "Jigoku no Yu-Badehaus":
//                break;
//
//            case "Kaidan":
//                break;
//
//            case "King Solomon":
//            case "Le roi Salomon":
//            case "König Salomon":
//                break;
//
//            case "League of Monster Slayers":
//            case "La Ligue des Chasseurs de Monstres":
//            case "Liga der Monsterjäger":
//                break;
//
//            case "Manufactory Breached":
//            case "La Manufacture envahie":
//            case "Manufaktur: Entblößt":
//                break;
//
//            case "Morninglight":
//            case "L'Aube Nouvelle":
//                break;
//
//            case "Museum":
//            case "Le musée":
//                break;
//
//            case "Nightmares in the Dream Palace":
//            case "Cauchemars au palaise des rêves":
//            case "Albtraum im Palast der Träume":
//                break;
//
//            case "Orochi Housing Projects":
//            case "Les logements collectifs Orochi":
//            case "Orochi-Sozialbauten":
//                break;
//
//            case "Orochi Tower":
//            case "La Tour Orochi":
//            case "Orochi-Turm":
//                break;
//
//            case "Reaping the Whirlwind":
//            case "Qui sème le vent":
//            case "Im Auge des Taifuns":
//                break;
//
//            case "Susanoo's Diner":
//            case "Restaurant Susanoo":
//                break;
//
//            case "Tale of Momotaro":
//            case "L'histoire de Momotaro":
//            case "Die Legende von Momotaro":
//                break;
//
//            case "Templars":
//            case "Templiers":
//            case "Templer":
//                break;
//
//            case "Temple City Discovery":
//            case "La découverte de la cité":
//            case "Fund in der Tempelstadt":
//                break;
//
//            case "The Abandoned":
//            case "Les laissés pour compte":
//            case "Der Verlassene":
//                break;
//
//            case "The Ankh":
//            case "L'Ankh":
//            case "Das Ankh":
//                break;
//
//            case "The Black House":
//            case "La Maison Noire":
//            case "Das schwarze Haus":
//                break;
//
//            case "The Black Signal":
//            case "Le Signal Noir":
//            case "Das schwarze Signal":
//                break;
//
//            case "The Bomb":
//            case "La bombe":
//            case "Die Bombe":
//                break;
//
//            case "The Buzzing":
//            case "Le bourdonnement":
//            case "Das Summen":
//                break;
//
//            case "The Call of the Nameless":
//            case "L'appel du Sans-Nom":
//            case "Der Ruf des Namenlosen":
//                break;
//
//            case "The Cargo":
//            case "Le cargo":
//            case "Die Fracht":
//                break;
//
//            case "The Collective Unconscious":
//            case "L'inconscient collectif":
//            case "Das kollektive Unbewusste":
//                break;
//
//            case "The Darkness War":
//            case "La Guerre des Ténèbres":
//            case "Der Krieg gegen die Finsternis":
//                break;
//
//            case "The Dream Palace":
//            case "Le Dream Palace":
//            case "Der Palast der Träume":
//                break;
//
//            case "The Filth":
//            case "La Souillure":
//            case "Der Schmutz":
//                break;
//
//            case "The Fog":
//            case "La brume":
//            case "Der Nebel":
//                break;
//
//            case "The Franklin Mansion":
//            case "Le Manoir Franklin":
//            case "Das Franklin-Anwesen":
//                break;
//
//            case "The Gungne":
//            case "Le Gungne":
//            case "Der Gungnir":
//                break;
//
//            case "The Hierarchy of Oni":
//            case "La hiérarchie des Oni":
//            case "Die Hierarchie der Oni":
//                break;
//
//            case "The Host":
//            case "Les Hôtes":
//            case "Der Wirt":
//                break;
//
//            case "The House-in-Exile":
//            case "La Maison en Exil":
//            case "Das Haus der Vertriebenen":
//                break;
//
//            case "The Jingu Clan":
//            case "Le clan Jingu":
//            case "Der Jingu-Clan":
//                break;
//
//            case "The Jinn and the First Age":
//            case "Les djinns et le Premier Âge":
//            case "Die Dschinn und das Erste Zeitalter":
//                break;
//
//            case "The Kingdom":
//            case "Le Royaume":
//            case "Das Reich":
//                break;
//
//            case "The Korinto-kai":
//            case "Les Korinto-kai":
//            case "Die Korinto-kai":
//                break;
//
//            case "The Lady Margaret":
//            case "Le Lady Margaret":
//            case "Die Lady Margaret":
//                break;
//
//            case "The Maiden Shrine":
//            case "Le Sanctuaire de la Vierge":
//            case "Der Jungfernschrein":
//                break;
//
//            case "The Manufactory":
//            case "La Manufacture":
//            case "Die Manufaktur":
//                break;
//
//            case "The Marya":
//            case "Les Maryas":
//            case "Die Marya":
//                break;
//
//            case "The Mitsubachi":
//            case "Les Mitsubachi":
//            case "Die Mitsubachi":
//                break;
//
//            case "The Ofuda":
//            case "Les ofuda":
//            case "Die Ofuda":
//                break;
//
//            case "The Orochi Group":
//            case "Le Groupe Orochi":
//            case "Die Orochi Group":
//                break;
//
//            case "Phoenicians":
//            case "Les Phéniciens":
//            case "Phönizier":
//                break;
//
//            case "The Polaris":
//            case "Le Polaris":
//            case "Die Polaris":
//                break;
//
//            case "The Pyramid":
//            case "La pyramide":
//            case "Die Pyramide":
//                break;
//
//            case "The Sentinels":
//            case "Les Sentinelles":
//            case "Die Wächter":
//                break;
//
//            case "The Third Age":
//            case "Le Troisième Âge":
//            case "Das Dritte Zeitalter":
//                break;
//
//            case "The Tragical History of Doctor Faustus":
//            case "La tragique histoire du docteur Faust":
//            case "Die tragische Historie vom Doktor Faustus":
//                break;
//
//            case "The Truce":
//            case "La trêve":
//            case "Der Waffenstillstand":
//                break;
//
//            case "The Wall":
//            case "Le mur":
//            case "Die Mauer":
//                break;
//
//            case "The Whispering Tide":
//            case "La marée chuchotante":
//            case "Die flüsternde Flut":
//                break;
//
//            case "Trail of Shadows":
//            case "La piste des ombres":
//            case "Spur der Schatten":
//                break;
//
//            case "Vampire Crusades":
//            case "Les croisades des vampires":
//            case "Vampir-Kreuzzüge":
//                break;
//
//            case "Zeroes Wild Pachinko":
//            case "La salle de pachinko Zeroes Wild":
//            case "Karinto Pachinko":
//                break;
//
//            case "Uta Bloody Valentine":
//            case "Uta Triple-Sang":
//            case "Uta - Blutiger Valentinstag":
//                break;
//
//            case "Anomalies of the Filth":
//            case "Anomalies de la Souillure":
//            case "Anomalien des Schmutzes":
//                break;
//
//            case "End of Days":
//            case "Fin des temps":
//            case "Ende aller Tage":
//                break;
//
//            case "Guardians of Gaia":
//            case "Gardiens de Gaia":
//            case "Bewacher von Gaia":
//                break;
//
//            case "Krampusnacht":
//                break;
//
//            case "Samhain 2012":
//                break;
//
//            case "Samhain 2013":
//                break;
//
//            case "Samhain 2014":
//                break;
//
//            case "Samhain 2015":
//                break;
//
//            case "Samhain 2016":
//                break;
//
//            case "The Manna of Saint Nicholas":
//            case "Le manne de Saint Nicolas":
//            case "Das Manna des heiligen Nikolaus":
//                break;
//
//            case "Ak'ab":
//            case "Les ak'abs":
//                break;
//
//            case "Arthropods":
//            case "Les arthropodes":
//            case "Gliederfüßer":
//                break;
//
//            case "Blajini":
//            case "Les Blajini":
//                break;
//
//            case "Bogeyman":
//            case "Le Croque-mitaine":
//            case "Der schwarze Mann":
//                break;
//
//            case "Cultists":
//            case "Le culte d'Aton":
//            case "Kultisten":
//                break;
//
//            case "Deathless":
//            case "Les Impérissables":
//            case "Todlose":
//                break;
//
//            case "Draug - Deep Ones":
//            case "Les Profonds":
//            case "Draugs - Tiefe Wesen":
//                break;
//
//            case "Draug - Drones":
//            case "Les draugs communs":
//            case "Draugs - Drohnen":
//                break;
//
//            case "Draug - Leaders":
//            case "Les chefs des draugs":
//            case "Draugs - Anführer":
//                break;
//
//            case "Familiars":
//            case "Les familiers":
//            case "Vertraute":
//                break;
//
//            case "Fauns":
//            case "Les faunes":
//            case "Faune":
//                break;
//
//            case "Filth and Humans":
//            case "La Souillure et les humains":
//            case "Schmutz und Menschen":
//                break;
//
//            case "Filth and Nature":
//            case "La Souillure et la nature":
//            case "Schmutz und Natur":
//                break;
//
//            case "Filth Guardians":
//            case "Les gardiens de la Souillure":
//            case "Schmutzverseuchte Bewacher":
//                break;
//
//            case "Ghouls":
//            case "Les goules":
//            case "Ghoule":
//                break;
//
//            case "Golems":
//            case "Les golems":
//                break;
//
//            case "Jinn":
//            case "Les djinns":
//            case "Dschinn":
//                break;
//
//            case "Kyonshi":
//            case "Les Kyonshi":
//                break;
//
//            case "Little Ones":
//            case "Les petits êtres":
//            case "Die Kleinen":
//                break;
//
//            case "Mummies":
//            case "Les momies":
//            case "Mumien":
//                break;
//
//            case "Namahage":
//            case "Les Namahage":
//                break;
//
//            case "Oni":
//            case "Les Oni":
//                break;
//
//            case "Orochi Tech":
//            case "La technologie Orochi":
//            case "Orochi-Technologie":
//                break;
//
//            case "Padurii":
//            case "Les Padurii":
//                break;
//
//            case "Rakshasa":
//            case "Les rakshasas":
//                break;
//
//            case "Revenants":
//            case "Les revenants":
//            case "Wiedergänger":
//                break;
//
//            case "Sasquatch":
//            case "Les sasquatchs":
//                break;
//
//            case "Scarecrows":
//            case "Les épouvantails":
//            case "Vogelscheuchen":
//                break;
//
//            case "Soldiers of Hell":
//            case "Les soldats des enfers":
//            case "Höllensoldaten":
//                break;
//
//            case "Spectres":
//            case "Les spectres":
//            case "Spukgestalten":
//                break;
//
//            case "Spirits of Flame":
//            case "Les esprites des flammes":
//            case "Geister der Flamme":
//                break;
//
//            case "Succubi and Incubi":
//            case "Les succubes et les incubes":
//                break;
//
//            case "Vampire Masters":
//            case "Les maîtres vampires":
//            case "Vampirmeister":
//                break;
//
//            case "Vampire Super Soldiers":
//            case "Les super-soldats vampires":
//            case "Vampir-Supersoldaten":
//                break;
//
//            case "Vampires":
//            case "Les vampires":
//            case "Vampire":
//                break;
//
//            case "Wendigo":
//            case "Les wendigos":
//                break;
//
//            case "Werewolves":
//            case "Les loups-garous":
//            case "Werwölfe":
//                break;
//
//            case "Wisps":
//            case "Les follets":
//            case "Irrlicht":
//                break;
//
//            case "Zmei":
//            case "Les Zmei":
//                break;
//
//            case "Zombies":
//            case "Les zombies":
//                break;
//
//            case "Blue Ridge Mine":
//            case "Mine de Blue Ridge":
//            case "Blue-Ridge-Mine":
//                break;
//
//            case "Iazmăciune":
//                break;
//
//            case "The Breach":
//            case "La Brèche":
//            case "Der Riss":
//                break;
//
//            case "Roman Baths":
//            case "Thermes romains":
//            case "Römische Bäder":
//                break;
//
//            case "The Facility":
//            case "Le Complexe":
//            case "Die Anlage":
//                break;
//
//            case "The Slaughterhouse":
//            case "L'Abattoir":
//            case "Das Schlachthaus":
//                break;
//
//            case "The Hell Dimensions":
//            case "Les dimensions infernales":
//            case "Die Höllendimensionen":
//                break;
//
//            case "Wabanaki":
//            case "Les Wabanakis":
//                break;
//
//            case "The Breaks in Time":
//            case "Les fissures du temps":
//            case "Die Brüche der Zeit":
//                break;
//
//            case "The Sleepless Lullaby":
//            case "Berceuse pour une nuit":
//            case "Das Schlaflied ohne Schlaf":
//                break;
//
//            case "The Sinking City":
//            case "La cité qui sombre":
//            case "Die versinkende Stadt":
//                break;
//
//            default:
//                locations.add(new LegendsLocation(1, "Kingsmouth", 250, 510, "Notes"));
//                locations.add(new LegendsLocation(2, "Kingsmouth", 250, 510, "Notes"));
//                locations.add(new LegendsLocation(3, "Kingsmouth", 250, 510, "Notes"));
//                break;
//        }
//    }
//}
