package application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.HostServices;

public class ExploreFactsController {
	
	private HostServices hostServices;		//allows to open external links
    public void setHostServices(HostServices hostServices) {	//allows another class to pass a object
        this.hostServices = hostServices;		//assigns the passed object
    }

    @FXML private TextField searchField;
    @FXML private TilePane countryTilePane;
    private List<Country> allCountries = new ArrayList<>();		//stores all countries for display and searching

    @FXML
    public void initialize() {
    
    	//slovenia
        Country slovenia = new Country(
                "Slovenia",
                "/application/resources/flags/slovenia.png",       // small flag for card
                "/application/resources/images/slovenia3.png",     // scenic image for details
                "https://www.lonelyplanet.com/destinations/slovenia"		  //link to the website
        );
        slovenia.addFact("“Slovenia is a watercolor painting—emerald lakes, mountain crowns, and caves that breathe secrets.”\n\n\nCapital : Ljubljana\nCurrency : Euro\nOfficial Language : Slovene\nBest Time to Visit : May-June and September.");
        slovenia.addFact("📍 Bled, Slovenia\n\n-Ljubljana, the capital, is known for its vibrant café culture and Baroque architecture.\n-Slovenia has over 10,000 caves, including Postojna Cave\n-You can visit Soča River which shines in emerald-green, perfect for rafting and kayaking.");
        allCountries.add(slovenia);

		 //Laos
        Country laos = new Country(
        		"Laos",
        		"/application/resources/flags/laos.png",
        		"/application/resources/images/laos.png",
        		"https://www.lonelyplanet.com/destinations/laos"
        );
		 laos.addFact("“Laos flows like a river dream—mist-cloaked mountains, gilded temples, and waterfalls cascading like liquid jade.”\n\n\nCapital: Vientiane\nCurrency: Lao Kip (LAK)\nOfficial Language: Lao\nBest Time to Visit: November–April (dry season)");
		 laos.addFact("📍 Somewhere in Laos\n\n-Laos is the only landlocked country in Southeast Asia, surrounded by mountains and rivers.\n-Often called the “Land of a Million Elephants,” it is rich in wildlife and culture.\n-The mighty Mekong River winds through the country, shaping life, trade, and tradition.");
		 allCountries.add(laos);
		 
		 //Samoa
		 Country samoa = new Country( "Samoa",
				 "/application/resources/flags/samoa.png",
				 "/application/resources/images/samoa.png",
				 "https://www.samoa.travel/"
		 );
		 samoa.addFact("“Samoa is an ocean hymn—turquoise waters and ancestral chants weaving time.”\n\n\nCapital: Apia\nCurrency: Samoan Tala (WST)\nOfficial Languages: Samoan, English\nBest Time to Visit: May–October.");
		 samoa.addFact("📍 Saletoga Sands, Samoa\n\n-Samoa is often called the “Cradle of Polynesia,” as it is considered the cultural heart of the Polynesian islands.\n-In 2011, Samoa skipped December 30 by moving the International Date Line to align its calendar with Australia and New Zealand for trade purposes.\n-Traditional Samoan culture, known as Fa’a Samoa, emphasizes family, respect, and community.\n-Samoa is rich in marine life, making it a popular destination for snorkeling and diving."
		 );
		 allCountries.add(samoa);
		 
		 // Malta
		 Country malta = new Country( 
				 "Malta",
				 "/application/resources/flags/malta.png",
				 "/application/resources/images/malta.png",
				 "https://www.visitmalta.com/en/"
		 );
		 malta.addFact("“Malta is a sun-drenched relic—stone temples and sapphire seas where myths still linger.”\n\n\nCapital: Valletta\nCurrency: Euro (EUR)\nOfficial Languages: Maltese, English\nBest Time to Visit: April–June, September–October.\n");
		 malta.addFact("📍 Mellieha,Malta\n\nMalta is home to megalithic temples that are even older than Stonehenge and the Pyramids of Giza, making it a treasure for history lovers.\n-Its stunning coastlines and medieval cities have been used as filming locations for major productions like Gladiator, Troy, and Game of Thrones.\n-The Maltese archipelago enjoys over 300 days of sunshine a year, making it a year-round travel destination."
		 );
		 allCountries.add(malta);
		 
		 //Kazakhstan
		  Country kazakhstan = new Country(
		  		"Kazakhstan",
		  		"/application/resources/flags/kazakh.png",
		  		"/application/resources/images/Kazakhstan.png",
		  		"https://www.lonelyplanet.com/destinations/kazakhstan"
		  );
		  kazakhstan.addFact("“Kazakhstan is an endless horizon—where sky swallows land, and eagles ride the wind.”\n\n\nCapital: Astana (still widely called Nur-Sultan)\nCurrency: Kazakhstani Tenge (KZT)\nOfficial Languages: Kazakh, Russian\nBest Time to Visit: April–June, September–October.");
		  kazakhstan.addFact("📍 Charyn Canyon, Tamerlik, Kazakhstan\n\n-Kazakhstan is the 9th largest country in the world, spanning vast steppes, deserts, and mountains across Central Asia.\n-Kazakhstan boasts incredible natural wonders, from the Charyn Canyon, often called the “Grand Canyon’s little brother,” to the shimmering Caspian Sea.\n-It is home to the Baikonur Cosmodrome, the world’s first and largest space launch facility");
		  allCountries.add(kazakhstan);
		  
		// Eritrea
		  Country eritrea = new Country(
		      "Eritrea",
		      "/application/resources/flags/eritrea.png",
		      "/application/resources/images/eritrea.png",
		      "https://www.lonelyplanet.com/destinations/eritrea"
		  );
		  eritrea.addFact("“Eritrea is a Red Sea jewel—deserts meet mountains, and history whispers in every corner.”\n\n\nCapital: Asmara\nCurrency: Eritrean Nakfa (ERN)\nOfficial Languages: Tigrinya, Arabic, English\nBest Time to Visit: October–April.");
		  eritrea.addFact("📍 Asmara, Eritrea\n\n-Eritrea is located in the Horn of Africa, bordering the Red Sea.\n-Its capital, Asmara, is a UNESCO World Heritage Site known for Italian colonial architecture.\n-The country has diverse landscapes, from the Danakil Desert to the Dahlak Archipelago.\n-Eritrea’s culture is a blend of African, Arab, and Italian influences, reflected in food, music, and festivals.");
		  allCountries.add(eritrea);	
		  
		  //estonia
		  Country estonia = new Country( 
				  "Estonia",
				  "/application/resources/flags/estonia.png",
				  "/application/resources/images/estonia.png",
				  "https://visitestonia.com/en"
		  );
		  estonia.addFact("“Estonia is a forest ballad—stone towers, digital dreams, and folklore carried on the wind.”\n\n\nCapital: Tallinn\nCurrency: Euro (EUR)\nOfficial Language: Estonian\nBest Time to Visit: May–August.");
		  estonia.addFact("📍 Old Town of Tallinn, Tallinn, Estonia\n\n-Known for its singing culture, Estonia hosts massive Song Festivals where tens of thousands join in choral traditions.\n-It’s one of the least crowded countries in Europe, so nature feels vast and untouched.\n-Estonia ranks among the fastest internet countries in the world, with Wi-Fi available even in forests and bogs.");
		  allCountries.add(estonia);
			 
		// Tunisia
		 Country tunisia = new Country( 
				 "Tunisia",
				 "/application/resources/flags/tunisia.png",
				 "/application/resources/images/tunisia.png",
				 "https://www.lonelyplanet.com/destinations/tunisia"
		 );
		 tunisia.addFact("“Tunisia is a desert tapestry—golden sands, blue seas, and ruins where empires echo.”\n\n\nCapital: Tunis\nCurrency: Tunisian Dinar (TND)\nOfficial Language: Arabic (French also used widel)\nBest Time to Visit: March–May, September–November");
		 tunisia.addFact("📍 Sidi Bou Said, Carthage, Tunisia\n\n-The Sahara Desert covers about 40% of Tunisia, with unique landscapes like salt flats and date-palm oases.\n-Many iconic Star Wars scenes were filmed in southern Tunisia—and fans can still visit the sets.\n-The island of Djerba is famous for its beaches and is said to be the “Land of the Lotus-Eaters” from Homer’s Odyssey.\n-Tunisia produces some of the world’s finest dates especially the Deglet Nour variety, known as the “queen of dates”.\n-Tunisia is home to the Great Mosque of Kairouan, one of the oldest and most important Islamic monuments in North Africa.");
		 allCountries.add(tunisia);
			 
		// Lesotho
		 Country lesotho = new Country( 
				 "Lesotho",
				 "/application/resources/flags/leso.png",
				 "/application/resources/images/lesotho.png",
				 "https://www.lonelyplanet.com/destinations/lesotho"
		 );
		 lesotho.addFact("“Lesotho is a mountain hymn—clouds touch the earth and waterfalls carve their song.”\n\n\nCapital: Maseru\nCurrency: Lesotho Loti (LSL) and South African Rand (ZAR)\nOfficial Languages: Sesotho, English\nBest Time to Visit: October–March\n"); 
		 lesotho.addFact("📍 Lesotho\n\n-Lesotho is called the “Kingdom in the Sky” because the entire country sits above 1,400 meters in elevation—the only nation in the world with this feature.\n-Despite its high altitude, Lesotho gets over 300 days of sunshine a year.\n-The Maloti Mountains are full of dinosaur footprints, some dating back 200 million years.");
		 allCountries.add(lesotho);
		 
		// Suriname
		 Country suriname = new Country(
		     "Suriname",
		     "/application/resources/flags/suri.png",
		     "/application/resources/images/suriname.png",
		     "https://www.lonelyplanet.com/destinations/the-guianas/suriname"
		 );
		 suriname.addFact("“Suriname is a tropical mosaic—rainforests whisper, rivers meander, and cultures blend in vibrant harmony.”\n\n\nCapital: Paramaribo\nCurrency: Surinamese Dollar (SRD)\nOfficial Language: Dutch\nBest Time to Visit: February–April, August–November");
		 suriname.addFact("📍 Suriname\n\n-Suriname is the smallest independent country in South America, but one of the most ethnically diverse.\n-Over 90% of its land is covered by tropical rainforest, making it a biodiversity hotspot.\n-The capital, Paramaribo, is a UNESCO World Heritage Site with Dutch colonial wooden architecture.\n-Suriname has strong cultural influences from Indian, African, Dutch, Indonesian, and Chinese heritage.");
		 allCountries.add(suriname);
		 
		// Botswana
		 Country botswana = new Country(
		     "Botswana",
		     "/application/resources/flags/bots.png",
		     "/application/resources/images/botswana.png",
		     "https://www.lonelyplanet.com/destinations/botswana"
		 );
		 botswana.addFact("“Botswana is wilderness eternal—where elephants roam free and rivers carve life into desert sands.”\n\n\nCapital: Gaborone\nCurrency: Botswana Pula (BWP)\nOfficial Language: English (Setswana widely spoken)\nBest Time to Visit: May–October (dry season)");
		 botswana.addFact("📍 Botswana\n\n-Botswana is home to the Okavango Delta, one of the world’s largest inland deltas and a UNESCO World Heritage Site.\n-More elephants live in Botswana than in any other country in the world.\n-The Kalahari Desert covers much of the nation, home to the San (Bushmen) people.\n-Botswana is one of Africa’s most politically stable and prosperous countries.");
		 allCountries.add(botswana);
		 
		// Lithuania
		 Country lithuania = new Country(
		     "Lithuania",
		     "/application/resources/flags/lithu.png",
		     "/application/resources/images/lithuania.png",
		     "https://www.lonelyplanet.com/destinations/lithuania"
		 );
		 lithuania.addFact("“Lithuania is a Baltic ballad—castle spires, forest whispers, and songs of resilience.”\n\n\nCapital: Vilnius\nCurrency: Euro (EUR)\nOfficial Language: Lithuanian\nBest Time to Visit: May–September");
		 lithuania.addFact("📍 Trakai, Lithuania\n\n-Lithuania was the first Soviet republic to declare independence in 1990.\n-Vilnius has one of the largest preserved medieval old towns in Europe.\n-The Hill of Crosses in northern Lithuania holds over 100,000 crosses, a symbol of faith and resistance.\n-Basketball is the most popular sport and a source of national pride.");
		 allCountries.add(lithuania);

		 // Cameroon
		 Country cameroon = new Country(
		     "Cameroon",
		     "/application/resources/flags/cam.png",
		     "/application/resources/images/cameroon.png",
		     "https://www.lonelyplanet.com/destinations/cameroon"
		 );
		 cameroon.addFact("“Cameroon is Africa in miniature—beaches, mountains, jungles, and cultures in dazzling variety.”\n\n\nCapital: Yaoundé\nCurrency: Central African CFA Franc (XAF)\nOfficial Languages: French, English\nBest Time to Visit: November–February");
		 cameroon.addFact("📍 Lagdo, Cameroon\n\n-Cameroon is known as ‘Africa in miniature’ because it has nearly every ecosystem: deserts, savannas, mountains, rainforests, and beaches.\n-Mount Cameroon, an active volcano, is the highest peak in West Africa.\n-It is home to over 250 ethnic groups, creating incredible cultural diversity.\n-Football (soccer) is a national passion, with legends like Samuel Eto’o born here.");
		 allCountries.add(cameroon);

		 // Nicaragua
		 Country nicaragua = new Country(
		     "Nicaragua",
		     "/application/resources/flags/nicara.png",
		     "/application/resources/images/nicaragua.png",
		     "https://www.lonelyplanet.com/destinations/nicaragua"
		 );
		 nicaragua.addFact("“Nicaragua is a land of fire and water—volcanoes rise beside lakes, and colonial cities glow with color.”\n\n\nCapital: Managua\nCurrency: Nicaraguan Córdoba (NIO)\nOfficial Language: Spanish\nBest Time to Visit: November–April");
		 nicaragua.addFact("📍 Cayo Crawl, Laguna de Perlas, Nicaragua\n\n-Nicaragua is known as the ‘Land of Lakes and Volcanoes,’ with over 19 volcanoes.\n-Lake Nicaragua is the largest lake in Central America and home to freshwater sharks.\n-The colonial cities of Granada and León are filled with colorful architecture.\n-Nicaragua offers some of the best surfing waves in Central America.");
		 allCountries.add(nicaragua);
		 
		// Slovakia
		 Country slovakia = new Country(
		     "Slovakia",
		     "/application/resources/flags/slovakia.png",
		     "/application/resources/images/slovakia.png",
		     "https://www.lonelyplanet.com/destinations/slovakia"
		 );
		 slovakia.addFact("“Slovakia is a castle dream—fairy-tale spires, mountain trails, and folk melodies in the air.”\n\n\nCapital: Bratislava\nCurrency: Euro (EUR)\nOfficial Language: Slovak\nBest Time to Visit: May–September");
		 slovakia.addFact("📍 The Ľubovňa Castle, Zámocká, Stará Ľubovňa, Slovakia\n\n-Slovakia has more than 100 castles and 6,000 caves packed into a small country.\n-The High Tatras are a paradise for hikers and skiers.\n-It shares borders with five countries, making it a crossroads of cultures.\n-Slovakia is famous for its traditional folk music and wooden architecture.");
		 allCountries.add(slovakia);
		 
		// Latvia
		 Country latvia = new Country(
		     "Latvia",
		     "/application/resources/flags/latvia.png",
		     "/application/resources/images/latvia.png",
		     "https://www.lonelyplanet.com/destinations/latvia"
		 );
		 latvia.addFact("“Latvia is a forested song—where medieval castles meet sandy beaches and folklore echoes.”\n\n\nCapital: Riga\nCurrency: Euro (EUR)\nOfficial Language: Latvian\nBest Time to Visit: May–September");
		 latvia.addFact("📍 Sigulda, Siguldas pilsēta, Latvia\n\n-Riga is famous for its Art Nouveau architecture and vibrant old town.\n-Latvia has beautiful sandy beaches along the Baltic coast.\n-The country has rich folk traditions including the Latvian Song and Dance Festival.");
		 allCountries.add(latvia);
		 
		// Bhutan
		 Country bhutan = new Country(
		     "Bhutan",
		     "/application/resources/flags/Bhutan.png",
		     "/application/resources/images/Bhutan.png",
		     "https://www.lonelyplanet.com/destinations/bhutan/"
		 );
		 bhutan.addFact("“Bhutan is a Himalayan sanctuary—where monasteries perch above valleys and happiness reigns supreme.”\n\n\nCapital: Thimphu\nCurrency: Bhutanese Ngultrum (BTN), Indian Rupee (INR)\nOfficial Language: Dzongkha\nBest Time to Visit: March–May, September–November");
		 bhutan.addFact("📍 Taktsang Trail, Paro, Bhutan\n\n-Bhutan is known for its Gross National Happiness philosophy.\n-Tiger's Nest Monastery is one of the most iconic cliffside temples.\n-The country preserves its culture and environment rigorously, making tourism limited and sustainable.");
		 allCountries.add(bhutan);

		 // Namibia
		 Country namibia = new Country(
		     "Namibia",
		     "/application/resources/flags/Namibia.png",
		     "/application/resources/images/namibia.png",
		     "https://www.lonelyplanet.com/destinations/namibia"
		 );
		 namibia.addFact("“Namibia is a desert symphony—red dunes, wildlife, and star-studded skies.”\n\n\nCapital: Windhoek\nCurrency: Namibian Dollar (NAD), South African Rand (ZAR)\nOfficial Language: English\nBest Time to Visit: May–October");
		 namibia.addFact("📍 Sandwich Harbour, Namibia\n\n-Namibia is home to the Namib Desert, the oldest desert in the world.\n-Etosha National Park is famous for wildlife safaris.\n-The country has striking landscapes including sand dunes, canyons, and the Skeleton Coast.");
		 allCountries.add(namibia);

		 // Belize
		 Country belize = new Country(
		     "Belize",
		     "/application/resources/flags/Belize.png",
		     "/application/resources/images/belize.png",
		     "https://www.lonelyplanet.com/destinations/belize"
		 );
		 belize.addFact("“Belize is a tropical jewel—jungles, reefs, and ancient Mayan whispers in the breeze.”\n\n\nCapital: Belmopan\nCurrency: Belize Dollar (BZD)\nOfficial Language: English\nBest Time to Visit: November–April");
		 belize.addFact("📍Ambergris Caye, Belize\n\n-Belize has the second-largest barrier reef in the world.\n-The country is rich in Mayan ruins such as Caracol and Altun Ha.\n-It has lush rainforests and rare wildlife, including jaguars and toucans.");
		 allCountries.add(belize);

		 // Albania
		 Country albania = new Country(
		     "Albania",
		     "/application/resources/flags/Albania.png",
		     "/application/resources/images/albania.png",
		     "https://www.lonelyplanet.com/destinations/albania"
		 );
		 albania.addFact("“Albania is a Mediterranean mosaic—mountains, beaches, and history in vibrant harmony.”\n\n\nCapital: Tirana\nCurrency: Albanian Lek (ALL)\nOfficial Language: Albanian\nBest Time to Visit: May–September");
		 albania.addFact("📍 Komani Lake, Albania\n\n-Albania has stunning beaches along the Ionian and Adriatic coasts.\n-The country is rich in Ottoman and ancient history, with castles and ruins.\n-The Albanian Alps are perfect for hiking and adventure tourism.");
		 allCountries.add(albania);

		 // Kyrgyzstan
		 Country kyrgyzstan = new Country(
		     "Kyrgyzstan",
		     "/application/resources/flags/Kyrgyzstan.png",
		     "/application/resources/images/kyrgyzstan.png",
		     "https://www.lonelyplanet.com/articles/guide-to-kyrgyzstan"
		 );
		 kyrgyzstan.addFact("“Kyrgyzstan is a nomad’s dream—mountains, lakes, and yurts beneath the endless sky.”\n\n\nCapital: Bishkek\nCurrency: Kyrgyzstani Som (KGS)\nOfficial Language: Kyrgyz, Russian\nBest Time to Visit: June–September");
		 kyrgyzstan.addFact("📍 Karakoł, Kirgistan\n\n-The country is known for its alpine lakes and the vast Tien Shan mountains.\n-Kyrgyz culture revolves around nomadic traditions and horseback games like Kok Boru.\n-Issyk-Kul Lake is the second-largest alpine lake in the world.");
		 allCountries.add(kyrgyzstan);

		// Sierra Leone
		 Country sierraLeone = new Country(
		     "Sierra Leone",
		     "/application/resources/flags/sleon.png",
		     "/application/resources/images/sier.png",
		     "https://www.lonelyplanet.com/destinations/sierra-leone"
		 );
		 sierraLeone.addFact("“Sierra Leone is an Atlantic gem—beaches, mountains, and forests teeming with life.”\n\n\nCapital: Freetown\nCurrency: Leone (SLL)\nOfficial Language: English\nBest Time to Visit: November–April");
		 sierraLeone.addFact("📍 Tiwai Island, Sierra Leone\n\n-The country has beautiful tropical beaches along the Atlantic coast.\n-It is rich in wildlife, including chimpanzees on Tiwai Island.\n-The terrain ranges from mountains to savannas, making it diverse for eco-tourism.");
		 allCountries.add(sierraLeone);

		 // Afghanistan
		 Country afghanistan = new Country(
		     "Afghanistan",
		     "/application/resources/flags/afhgan.png",
		     "/application/resources/images/afghanistan.png",
		     "https://www.lonelyplanet.com/destinations/afghanistan"
		 );
		 afghanistan.addFact("“Afghanistan is a land of rugged beauty—mountains, deserts, and ancient civilizations.”\n\n\nCapital: Kabul\nCurrency: Afghani (AFN)\nOfficial Languages: Pashto, Dari\nBest Time to Visit: April–October");
		 afghanistan.addFact("📍 Band-e Amir, Afghanistan\n\n-Afghanistan has a rich history with ancient Silk Road sites.\n-The Hindu Kush mountains dominate the landscape.\n-The country is home to historic cities like Herat, Balkh, and Kandahar.");
		 allCountries.add(afghanistan);

		 // Fiji
		 Country fiji = new Country(
		     "Fiji",
		     "/application/resources/flags/fiji.png",
		     "/application/resources/images/fiji.png",
		     "https://www.lonelyplanet.com/destinations/fiji"
		 );
		 fiji.addFact("“Fiji is a Pacific paradise—coral reefs, turquoise lagoons, and island rhythms.”\n\n\nCapital: Suva\nCurrency: Fijian Dollar (FJD)\nOfficial Languages: English, Fijian, Hindi\nBest Time to Visit: May–October");
		 fiji.addFact("📍 Makogai Island, Fiji\n\n-Fiji is known for its tropical beaches and coral reefs.\n-The islands are famous for snorkeling, diving, and cultural experiences.\n-Fijian culture emphasizes community, dance, and traditional ceremonies.");
		 allCountries.add(fiji);

		 // Morocco
		 Country morocco = new Country(
		     "Morocco",
		     "/application/resources/flags/mrc.png",
		     "/application/resources/images/morocco.png",
		     "https://www.lonelyplanet.com/destinations/morocco"
		 );
		 morocco.addFact("“Morocco is a mosaic of colors—deserts, medinas, and mountain trails.”\n\n\nCapital: Rabat\nCurrency: Moroccan Dirham (MAD)\nOfficial Language: Arabic, Berber\nBest Time to Visit: March–May, September–November");
		 morocco.addFact("📍 Chefchaouen, Morocco\n\n-Morocco is famous for the Sahara Desert and Atlas Mountains.\n-Cities like Marrakech, Fes, and Chefchaouen offer vibrant markets and historic medinas.\n-The country has a rich culinary tradition with spices and tagines.");
		 allCountries.add(morocco);

		 // Panama
		 Country panama = new Country(
		     "Panama",
		     "/application/resources/flags/pnama.png",
		     "/application/resources/images/panama.png",
		     "https://www.lonelyplanet.com/destinations/panama"
		 );
		 panama.addFact("“Panama is a bridge of continents—jungles, canals, and vibrant cultures.”\n\n\nCapital: Panama City\nCurrency: Balboa (PAB), US Dollar (USD)\nOfficial Language: Spanish\nBest Time to Visit: December–April");
		 panama.addFact("📍 Casco Viejo, Panama City, Panama\n\n-Panama is famous for the Panama Canal connecting the Atlantic and Pacific Oceans.\n-The country has tropical rainforests and islands on both coasts.\n-Panama City features a modern skyline alongside historic Casco Viejo.");
		 allCountries.add(panama);

		 // Andorra
		 Country andorra = new Country(
		     "Andorra",
		     "/application/resources/flags/andora.png",
		     "/application/resources/images/andorra.png",
		     "https://www.lonelyplanet.com/destinations/andorra"
		 );
		 andorra.addFact("“Andorra is a Pyrenean jewel—mountains, ski slopes, and medieval streets.”\n\n\nCapital: Andorra la Vella\nCurrency: Euro (EUR)\nOfficial Language: Catalan\nBest Time to Visit: December–April for skiing, June–September for hiking");
		 andorra.addFact("📍 Andorra\n\n-Andorra is famous for skiing and winter sports.\n-It has medieval churches and tax-free shopping.\n-The country is nestled between France and Spain in the Pyrenees Mountains.");
		 allCountries.add(andorra);

		 // São Tomé and Príncipe
		 Country saotome = new Country(
		     "Sao Tome and Príncipe",
		     "/application/resources/flags/sao.png",
		     "/application/resources/images/saotome.png",
		     "https://www.lonelyplanet.com/articles/first-time-guide-to-sao-tome-principe"
		 );
		 saotome.addFact("“São Tomé and Príncipe is an equatorial paradise—volcanoes, cocoa plantations, and azure seas.”\n\n\nCapital: São Tomé\nCurrency: Dobra (STN)\nOfficial Language: Portuguese\nBest Time to Visit: June–September");
		 saotome.addFact("📍 São Tomé Island\n\n-The islands are famous for cocoa production and lush landscapes.\n-They have pristine beaches and volcanic craters.\n-The islands are rich in biodiversity, including endemic birds and butterflies.");
		 allCountries.add(saotome);

		 // Kiribati
		 Country kiribati = new Country(
		     "Kiribati",
		     "/application/resources/flags/kiri.png",
		     "/application/resources/images/kiribati.png",
		     "https://www.lonelyplanet.com/destinations/kiribati"
		 );
		 kiribati.addFact("“Kiribati is an ocean embrace—atolls, lagoons, and traditions under the sun.”\n\n\nCapital: South Tarawa\nCurrency: Australian Dollar (AUD)\nOfficial Language: English\nBest Time to Visit: May–October");
		 kiribati.addFact("📍 Tarawa Atoll, Kiribati\n\n-Kiribati consists of 33 atolls spread across the central Pacific.\n-It is known for marine biodiversity and traditional fishing culture.\n-The islands experience early sunrises, being near the International Date Line.");
		 allCountries.add(kiribati);

		 // Tajikistan
		 Country tajikistan = new Country(
		     "Tajikistan",
		     "/application/resources/flags/tzy.png",
		     "/application/resources/images/tajikistan.png",
		     "https://www.lonelyplanet.com/destinations/tajikistan"
		 );
		 tajikistan.addFact("“Tajikistan is a mountain jewel—peaks, rivers, and Silk Road echoes.”\n\n\nCapital: Dushanbe\nCurrency: Somoni (TJS)\nOfficial Language: Tajik\nBest Time to Visit: May–September");
		 tajikistan.addFact("📍 Tajikistan\n\n-The country is mountainous and landlocked in Central Asia.\n-It is famous for hiking, trekking, and Silk Road historical sites.\n-The Pamir Highway offers one of the most scenic road trips in the world.");
		 allCountries.add(tajikistan);
		 
         displayCountries(allCountries);	//displays
         
        //search function
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
        	//searchField.setCursor(Cursor.TEXT);
        	//searchField.setCursor(Cursor.CROSSHAIR);
            List<Country> filtered = allCountries.stream()			//stream allows operation like filters
                    .filter(c -> c.getName().toLowerCase().contains(newVal.toLowerCase()))
                    .collect(Collectors.toList());		//converts to list from stream again
            displayCountries(filtered);
        });
    }

    private void displayCountries(List<Country> countries) {
        countryTilePane.getChildren().clear();
        for (Country country : countries) {
            Pane card = createCountryCard(country);		//creates card for individual country
            countryTilePane.getChildren().add(card);	//adds the country to the tilepane
        }
    }

    private Pane createCountryCard(Country country) {
        Pane cardWrapper = new Pane();
        cardWrapper.setPrefSize(300, 200);
        cardWrapper.setPadding(new Insets(15, 15, 15, 15));
        cardWrapper.getStyleClass().add("country-card");

        // Loads flag
        Image flagImage;
        try {
            flagImage = new Image(getClass().getResourceAsStream(country.getFlagPath()));
        } catch (Exception e) {
            System.out.println("Image not found: " + country.getFlagPath());
            flagImage = new Image(getClass().getResourceAsStream("/application/resources/flags/kiri.png"));		//if some flag images are missing it will show this default one
        }
        
        ImageView flag = new ImageView(flagImage);
        flag.setFitWidth(250);
        flag.setFitHeight(150);
        flag.setPreserveRatio(true);

        // Country Name label
        Label name = new Label(country.getName());
        name.getStyleClass().add("country-name");

        // VBox for flag + name
        VBox content = new VBox(10, flag, name); 	// 10px spacing between flag and countryname
        content.setAlignment(Pos.CENTER);
        content.setPrefSize(290, 200);

        cardWrapper.getChildren().add(content);
        cardWrapper.setOnMouseClicked(e -> openCountryDetails(country));

        return cardWrapper;
    }

    //Country model
    public static class Country {
        private String name;
        private List<String> facts = new ArrayList<>();
        private String flagPath;       // for card
        private String scenicImagePath; // for details page
        private String learnMoreLink;

        public Country(String name, String flagPath, String scenicImagePath, String learnMoreLink) {
            this.name = name;
            this.flagPath = flagPath;
            this.scenicImagePath = scenicImagePath;
            this.learnMoreLink = learnMoreLink;
        }

        public void addFact(String fact) {
            if (fact != null)
            	facts.add(fact);
        }

        public String getName() { return name; }
        public List<String> getFacts() { return facts; }
        public String getFlagPath() { return flagPath; }
        public String getScenicImagePath() { return scenicImagePath; }
        public String getLearnMoreLink() { return learnMoreLink; }
    }

    @FXML
    private void openCountryDetails(Country country) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CountryDetails.fxml"));
            Parent root = loader.load();

            CountryDetailsController controller = loader.getController();
            controller.setCountryData(country);
            controller.setHostServices(this.hostServices);		// Passes the HostServices

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setTitle(country.getName());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBackToMainMenu(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MainMenu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.setCursor(Cursor.HAND);
            stage.setScene(scene);
            stage.setTitle("Factrip - Main Menu");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
