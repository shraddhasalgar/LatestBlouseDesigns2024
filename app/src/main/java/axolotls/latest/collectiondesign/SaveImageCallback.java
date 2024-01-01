package axolotls.latest.collectiondesign;

import android.net.Uri;

interface SaveImageCallback {
    void onSaveComplete(Uri imageUri);

    void onSaveFailed();
}
