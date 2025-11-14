#!/bin/bash

cd /home/user/colorglow-privacy-policy/all-status-studio/app/src/main/res/layout

# Create all activity layouts with basic structure
for activity in onboarding whatsapp gallery editor templates caption scheduler cleaner vault app_lock settings; do
  cat > activity_${activity}.xml << EOF
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="@color/background"
        app:titleTextColor="@color/white"
        app:layout_constraintTop_toTopOf="parent"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@id/toolbar"
        app:layout_constraintBottom_toBottomOf="parent"/>

    <TextView
        android:id="@+id/emptyView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="No items found"
        android:textColor="@color/white"
        android:visibility="gone"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
EOF
done

# Create item layouts
for item in status gallery template caption cleaner onboarding_slide; do
  cat > item_${item}.xml << EOF
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardBackgroundColor="@color/card_background"
    app:cardCornerRadius="12dp"
    app:strokeColor="@color/neon_purple"
    app:strokeWidth="1dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="12dp">

        <ImageView
            android:id="@+id/ivThumbnail"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:scaleType="centerCrop"
            android:background="@color/card_background"/>

        <ImageView
            android:id="@+id/ivPlayIcon"
            android:layout_width="48dp"
            android:layout_height="48dp"
            android:src="@drawable/ic_play"
            android:layout_gravity="center"
            android:visibility="gone"/>

        <TextView
            android:id="@+id/tvTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="@color/white"
            android:textSize="14sp"
            android:layout_marginTop="8dp"/>

        <TextView
            android:id="@+id/tvCaption"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="@color/white"
            android:textSize="16sp"/>

        <TextView
            android:id="@+id/tvCategory"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="@color/neon_cyan"
            android:textSize="12sp"/>

        <TextView
            android:id="@+id/tvDescription"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="@color/white"
            android:textSize="14sp"/>

        <TextView
            android:id="@+id/tvFileName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="@color/white"
            android:textSize="14sp"/>

        <TextView
            android:id="@+id/tvFileType"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="@color/neon_pink"
            android:textSize="12sp"/>

        <TextView
            android:id="@+id/tvFileSize"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="@color/white"
            android:textSize="12sp"/>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="end"
            android:layout_marginTop="8dp">

            <ImageButton
                android:id="@+id/btnSave"
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:src="@drawable/ic_download"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:tint="@color/neon_cyan"/>

            <ImageButton
                android:id="@+id/btnShare"
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:src="@drawable/ic_share"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:tint="@color/neon_pink"/>

            <ImageButton
                android:id="@+id/btnFavorite"
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:src="@drawable/ic_favorite_outline"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:tint="@color/neon_purple"/>

            <ImageButton
                android:id="@+id/btnCopy"
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:src="@drawable/ic_copy"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:tint="@color/neon_cyan"/>

            <CheckBox
                android:id="@+id/checkbox"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:buttonTint="@color/neon_purple"/>

            <ImageView
                android:id="@+id/ivPremium"
                android:layout_width="24dp"
                android:layout_height="24dp"
                android:src="@drawable/ic_premium"
                android:tint="@color/gold"/>

            <com.airbnb.android.lottie.LottieAnimationView
                android:id="@+id/lottieAnimation"
                android:layout_width="200dp"
                android:layout_height="200dp"
                android:layout_gravity="center"/>

        </LinearLayout>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
EOF
done

echo "Layouts created successfully"
