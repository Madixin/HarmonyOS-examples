package com.example.mynews.util;


import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.media.image.common.Rect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class CommonUtils {
    private static final String TAG = "Common Utils";
    private static final int GET_COLOR_STATE_FAILED = -1;
    private static final int JSON_READ_ERROR = -1;


    /**
     * Obtains a List based on the resource path.
     *
     * @param context context
     * @param jsonPath path
     * @return jsonString
     */
    public static String getStringFromJsonPath(Context context, String jsonPath) {
        Resource datasResource;
        try {
            datasResource = context.getResourceManager().getRawFileEntry(jsonPath).openRawFile();
            byte[] buffers = new byte[datasResource.available()];
            if (datasResource.read(buffers) != JSON_READ_ERROR) {
                return new String(buffers, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            LogUtils.e(TAG, "some error happened");
        }
        return Optional.of(jsonPath).toString();
    }


    /**
     * Obtains a bitmap object based on the resource path.
     *
     * @param context context
     * @param path path
     * @return pixelMap
     */
    public static PixelMap getPixelMapFromPath(Context context, String path) {
        InputStream drawableInputStream = null;
        try {
            drawableInputStream = context.getResourceManager().getRawFileEntry(path).openRawFile();
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(drawableInputStream, sourceOptions);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            decodingOptions.desiredSize = new Size(0, 0);
            decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
            decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
            return imageSource.createPixelmap(decodingOptions);
        } catch (IOException e) {
            LogUtils.i(TAG, "some error happended");
        } finally {
            try {
                if (drawableInputStream != null) {
                    drawableInputStream.close();
                }
            } catch (IOException e) {
                LogUtils.e(TAG, "some error happened");
            }
        }
        return null;
    }
}
