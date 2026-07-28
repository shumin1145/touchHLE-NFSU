/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Parts of this file are derived from SDL 2's Android project template, which
 * has a different license. Please see vendor/SDL/LICENSE.txt for details.
 */
package com.ea.nfsucbvzh;

import android.os.Bundle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.libsdl.app.SDLActivity;

/**
 * A wrapper class over SDLActivity.
 *
 * On first launch, the bundled IPA is copied from the APK's assets to the
 * touchHLE apps directory on external storage. The path is then passed to the
 * native entry point so the game starts directly, without showing the app
 * picker.
 */

public class MainActivity extends SDLActivity {
    private static final String APPS_DIR = "touchHLE_apps";
    private static final String IPA_NAME = "极品飞车12.ipa";
    private static final String ASSET_PATH = APPS_DIR + "/" + IPA_NAME;
    private static final int COPY_BUFFER_SIZE = 8192;

    @Override
    protected String[] getLibraries() {
        return new String[]{
            "SDL2",
            "touchHLE"
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        copyBundledIpaIfNeeded();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String[] getArguments() {
        File base = getExternalFilesDir(null);
        if (base != null) {
            File ipa = new File(new File(base, APPS_DIR), IPA_NAME);
            if (ipa.exists()) {
                return new String[] { ipa.getAbsolutePath() };
            }
        }
        return new String[0];
    }

    private void copyBundledIpaIfNeeded() {
        File base = getExternalFilesDir(null);
        if (base == null) {
            return;
        }
        File appsDir = new File(base, APPS_DIR);
        if (!appsDir.isDirectory() && !appsDir.mkdirs()) {
            return;
        }
        File target = new File(appsDir, IPA_NAME);
        if (target.exists()) {
            return;
        }

        try (InputStream is = getAssets().open(ASSET_PATH);
             OutputStream os = new FileOutputStream(target)) {
            byte[] buf = new byte[COPY_BUFFER_SIZE];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
