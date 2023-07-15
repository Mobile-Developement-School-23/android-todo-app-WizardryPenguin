//package ru.winpenguin.todoapp
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import androidx.core.app.NotificationCompat
//
//class Notification : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
//            .setContentText("This is some context text")
//            .setContentTitle("Hello world!")
//            .setSmallIcon(R.drawable.info_outline)
//            .build()
//    }
//
//}

//@Composable
//private fun NotificationStaff() {
//    val context = LocalContext.current
//    var hasNotificationPermission by remember {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            mutableStateOf(
//                ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED
//            )
//        } else mutableStateOf(true)
//    }
//    val permissionLauncer = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            hasNotificationPermission = isGranted
////                        if (!isGranted) {
////                            shouldShowRequestPermissionRationale()
////                        }
//        })
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                permissionLauncer.launch(Manifest.permission.POST_NOTIFICATIONS)
//            }
//        }) {
//            Text(text = "Request permission")
//        }
//
//        Button(onClick = {
//            if (hasNotificationPermission) {
//                showNotification()
//            }
//        }) {
//            Text(text = "Show notification")
//        }
//    }
//}
//
//private fun showNotification() {
//    val notificationManager =
//        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    val notification = NotificationCompat.Builder(applicationContext, "channel_id")
//        .setContentText("This is some context text")
//        .setContentTitle("Hello world!")
//        .setSmallIcon(R.drawable.info_outline)
//        .build()
//    notificationManager.notify(1, notification)
//}