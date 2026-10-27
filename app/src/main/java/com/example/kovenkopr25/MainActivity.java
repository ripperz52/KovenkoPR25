package com.example.kovenkopr25;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;
    // Идентификатор канала
    private static final String CHANNEL_ID = "Cat channel";
    // Код запроса разрешения
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создаём канал уведомлений (обязательно для API 26+)
        createNotificationChannel();

        // Задание 1: кнопка для показа простого уведомления
        Button buttonShow = findViewById(R.id.buttonShowNotification);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleNotification();
            }
        });

        // Задание 4: уведомление с длинным текстом (BigTextStyle)
        Button buttonBigText = findViewById(R.id.buttonBigText);
        buttonBigText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBigTextNotification();
            }
        });

        // Задание 4: уведомление с тремя кнопками
        Button buttonActions = findViewById(R.id.buttonActions);
        buttonActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionNotification();
            }
        });
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Cat Channel";
            String description = "Канал для уведомлений о коте";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    private void showSimpleNotification() {
        if (!checkNotificationPermission()) {
            return;
        }

        // Задание 2: создаём Intent и PendingIntent для реакции на нажатие
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Задание 1 + Задание 3: строим уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pets_black_24dp)
                .setContentTitle("Напоминание")
                .setContentText("Пора покормить кота")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                // Задание 3: дополнительные настройки
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.hungrycat))
                .setTicker("Последнее китайское предупреждение!")
                .setAutoCancel(true)
                .setColor(Color.GREEN);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }


    private void showBigTextNotification() {
        if (!checkNotificationPermission()) {
            return;
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pets_black_24dp)
                .setContentTitle("Посылка")
                .setContentText("Это я, почтальон Печкин. Принёс для вас посылку")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.hungrycat))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Это я, почтальон Печкин. Принёс для вас посылку. " +
                                "Только я вам её не отдам. Потому что у вас документов нету."))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID + 1, builder.build());
    }


    private void showActionNotification() {
        if (!checkNotificationPermission()) {
            return;
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pets_black_24dp)
                .setContentTitle("Посылка")
                .setContentText("Это я, почтальон Печкин. Принёс для вас посылку")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.hungrycat))
                .addAction(R.drawable.ic_pets_black_24dp, "Открыть", pendingIntent)
                .addAction(R.drawable.ic_pets_black_24dp, "Отказаться", pendingIntent)
                .addAction(R.drawable.ic_pets_black_24dp, "Другой вариант", pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID + 2, builder.build());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено — можно показать уведомление
                showSimpleNotification();
            }
        }
    }
}