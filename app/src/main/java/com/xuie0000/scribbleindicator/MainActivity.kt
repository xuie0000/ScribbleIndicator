package com.xuie0000.scribbleindicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.xuie0000.scribbleindicator.ui.theme.ScribbleIndicatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ScribbleIndicatorTheme {
                ScribbleSample()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScribbleSample() {
    val pagerState: PagerState = rememberPagerState { indicatorList.size }

    Scaffold { paddings ->
        ScribbleIndicator(
            list = indicatorList,
            modifier = Modifier.padding(paddings).padding(vertical = 16.dp).fillMaxSize(),
            pagerState = pagerState,
            title = { item ->
                SampleTitle(item)
            },
            content = {
                PagerContent(pagerState = pagerState, list = indicatorList)
            },
        )
    }
}

@Composable
fun ColumnScope.SampleTitle(recipe: Recipe) {
    Text(
        text = recipe.title.uppercase(),
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
        ),
        modifier = Modifier
            .align(CenterHorizontally)
            .padding(horizontal = 32.dp, vertical = 16.dp),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(pagerState: PagerState, list: List<Recipe>) {
    HorizontalPager(
        state = pagerState,
        beyondBoundsPageCount = 10,
    ) { page ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
        ) {
            AsyncImage(
                model = list[page].imgURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(3 / 2f)
                    .fillMaxWidth()
                    .offset(20.dp, 20.dp)
                    .background(Color(0xFFDAAD90), shape = RoundedCornerShape(20.dp))
                    .offset((-20).dp, (-20).dp)
                    .clip(RoundedCornerShape(20.dp)),
            )

            Box(Modifier.height(30.dp))

            Text(
                text = list[page].title,
                fontWeight = FontWeight.Black,
                fontSize = 42.sp,
                color = Color(0xFF362C28),
            )
            Box(Modifier.height(7.dp))
            Text(text = list[page].description)
        }
    }
}

data class Recipe(
    val title: String,
    val description: String,
    val imgURL: String,
)

private val indicatorList = listOf(
    Recipe(
        "Schnitzel",
        "A thin cutlet of meat (usually pork or veal) that is breaded and fried until crispy.",
        "https://www.familienkost.de/images/familienkostbild_1248_schnitzel.jpg",
    ),
    Recipe(
        "Sauerbraten",
        "A type of pot roast that is marinated in a mixture of vinegar, spices, and red wine for several days before being cooked.",
        "https://www.koch-mit.de/app/uploads/2020/03/sauerbraten.jpg",
    ),
    Recipe(
        "Kartoffelpuffer",
        "Also known as potato pancakes, these are made by shredding potatoes and mixing them with flour, eggs, and onions before frying them until crispy.",
        "https://www.gutekueche.at/storage/media/recipe/107893/conv/kartoffelpuffer-default.jpg",
    ),
    Recipe(
        "Spätzle",
        "A type of soft egg noodle that is typically served with a variety of dishes, such as roasted meats or stews.",
        "https://die-frau-am-grill.de/wp-content/uploads/spaetzle-rezept-istock.jpg",
    ),
    Recipe(
        "Rouladen",
        "Thinly sliced beef that is rolled up with onions, mustard, and bacon before being braised in a red wine sauce.",
        "https://www.globus.de/media/globus/mio/magazin/ausgaben-2022/dezember_2022/roullade_rotkohl_169.jpg",
    ),
    Recipe(
        "Königsberger Klopse",
        "Meatballs made from ground beef or veal that are simmered in a creamy white sauce with capers, lemon juice, and anchovies.",
        "https://www.malteskitchen.de/wp-content/uploads/2022/01/koenigsberger-klopse-blogpost-5167-500x500.jpg",
    ),
    Recipe(
        "Schwarzwälder Kirschtorte",
        "Also known as Black Forest Cake, this is a chocolate sponge cake that is layered with whipped cream and cherries before being topped with chocolate shavings and a splash of cherry brandy.",
        "https://img.chefkoch-cdn.de/rezepte/463131139405875/bilder/1369608/crop-960x720/schwarzwaelder-kirschtorte-super-easy.jpg",
    ),
    Recipe(
        "Leberknödel",
        "Liver dumplings made from ground liver, bread crumbs, and spices. They are typically served in a beef broth and accompanied by Spätzle or boiled potatoes.",
        "https://www.servus.com/storage/recipe/rezept-vorspeise-suppe-innereien-gebackene-leberknodel-suppe.jpg?impolicy=recipe_head",
    ),
    Recipe(
        "Labskaus",
        "A type of stew made from corned beef, onions, and potatoes that is typically served with pickled herring and beetroot.",
        "https://images.eatsmarter.de/sites/default/files/styles/max_size/public/labskaus-nach-hamburger-art-44811.jpg",
    ),
    Recipe(
        "Weißwurst",
        "A type of sausage that is made from veal and pork and flavored with parsley, lemon, and cardamom. It is typically served with sweet mustard, pretzels, and a glass of beer.",
        "https://metzgereiwendel.de/wp-content/uploads/2015/11/Weisswurst.jpg",
    ),
)
