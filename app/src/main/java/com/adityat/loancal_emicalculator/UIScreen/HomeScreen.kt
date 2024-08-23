package com.adityat.loancal_emicalculator.UIScreen


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adityat.loancal_emicalculator.Navigation
import com.adityat.loancal_emicalculator.Screen
import com.adityat.loancal_emicalculator.entityScreens
import com.adityat.loancal_emicalculator.screenInDrawer
import com.adityat.loancal_emicalculator.viewModels.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val scope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val viewModel: MainViewModel = viewModel()

    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current

    val allScreens = screenInDrawer + entityScreens

    val title = remember {
        mutableStateOf(viewModel.currentScreen.value.title)
    }

    // Update the title based on the current route
    currentRoute?.let { route ->
        title.value = allScreens.find { it.route == route }?.title ?: "Loan Calculator"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(64.dp),
                backgroundColor = Color.White,
                title = {
                    Text(
                        text = title.value,
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(4.dp))
                            .padding(5.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Open the drawer
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "",
                            tint = Color(0xff222E50),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
            )
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {

                Column(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "LonaLo : EMI CALCULATOR",
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                                fontSize = 24.sp,
                                textDecoration = TextDecoration.Underline,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Calculate . Track . Evaluate",
                                modifier = Modifier.padding(top = 20.dp, start = 16.dp),
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.SemiBold
                            )

                        }

                    }
                    Text(
                        text = "Calculator",
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyColumn(Modifier.padding(16.dp)) {
                        items(screenInDrawer) { item ->
                            DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                    if(item.route == Screen.DrawerScreens.Sharethisapp.route){
                                        val shareIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: [Your App Link]")
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                                    }
                                    else if(item.route == Screen.DrawerScreens.Ratethisapp.route){
                                        val packageName = context.packageName
                                        val uri = Uri.parse("market://details?id=$packageName")
                                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                                        try {
                                            context.startActivity(goToMarket)
                                        } catch (e: ActivityNotFoundException) {
                                            // If the Play Store app is not installed, open the app in a browser
                                            val webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                                            context.startActivity(webIntent)
                                        }
                                    }
                                    else if(item.route == Screen.DrawerScreens.Feedbackreport.route){
                                        val email = "at401646@gmail.com" // Replace with your email
                                        val subject = "App Feedback"
                                        val body = "Please write your feedback here..."

                                        val uri = Uri.parse("mailto:")
                                            .buildUpon()
                                            .appendQueryParameter("to", email)
                                            .appendQueryParameter("subject", subject)
                                            .appendQueryParameter("body", body)
                                            .build()

                                        val emailIntent = Intent(Intent.ACTION_SENDTO, uri).apply {
                                            setPackage("com.google.android.gm") // Specify the Gmail package
                                        }

                                        context.startActivity(emailIntent)
                                    }
                                    else if(item.route == Screen.DrawerScreens.Privacypolicies.route){
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.termsfeed.com/live/f2b83754-d0ee-4d42-ba12-316a90c07384"))
                                        context.startActivity(intent)

                                    }
                                    else{
                                        controller.navigate(item.dRoute)
                                    }

                                }

                            }
                        }
                    }
                    Divider()

                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = {
                            AdView(it).apply {
                                setAdSize(AdSize.SMART_BANNER)
                                adUnitId = "ca-app-pub-1838194983985161/1165697539"
                                loadAd(AdRequest.Builder().build())
                            }
                        })
                }
            }
        }
    ) {
        Navigation(navController = controller, viewModel = viewModel, pd = it)
    }
}


@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.DrawerScreens,
    onDrawerItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable {
                onDrawerItemClick()
            },

        verticalAlignment = Alignment.CenterVertically,


        ) {
        Icon(
            imageVector = item.icon,
            contentDescription = "",
            modifier = Modifier
                .padding(top = 4.dp, end = 8.dp)
                .size(28.dp),
            tint = Color(0xff0174A3)
        )
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            color = Color.Black

        )

    }

}

