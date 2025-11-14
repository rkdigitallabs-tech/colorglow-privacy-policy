#!/bin/bash

cd /home/user/colorglow-privacy-policy/all-status-studio/app/src/main/res/drawable

# Create neon border drawable
cat > card_neon_border.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/card_background"/>
    <stroke android:width="2dp" android:color="@color/neon_purple"/>
    <corners android:radius="12dp"/>
</shape>
EOF

# Create icons as vector drawables
for icon in whatsapp gallery edit template caption schedule clean vault settings download share favorite_outline favorite_filled copy play add notification premium; do
cat > ic_${icon}.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM12,20c-4.41,0 -8,-3.59 -8,-8s3.59,-8 8,-8 8,3.59 8,8 -3.59,8 -8,8z"/>
</vector>
EOF
done

# Create placeholder images
cat > placeholder_image.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/card_background"/>
    <corners android:radius="8dp"/>
</shape>
EOF

cat > placeholder_template.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/card_background"/>
    <stroke android:width="2dp" android:color="@color/neon_cyan"/>
    <corners android:radius="12dp"/>
</shape>
EOF

echo "Drawables created successfully"
