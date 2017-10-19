package com.print.printer;

import android.text.TextUtils;

import com.print.printer.hardware.printer.PosPrinter;
import com.print.printer.hardware.printer.PrintUtils;
import com.print.printer.hardware.utils.StringUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <pre>
 *      author : shihaoyu
 *      e-mail : shihaoyu@propersoft.cn
 *      time   : 2017/10/17
 *      desc   :
 *      version: 1.0
 *  </pre>
 */

public class CustomPrint extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("printText".equals(action)) {
            JSONObject jsonObject = new JSONObject(args.get(0).toString());
            int alignment = jsonObject.isNull("alignment") ?
                    PosPrinter.TEXT_ALIGNMENT_LEFT : jsonObject.getInt("alignment");
            int lineHeight = jsonObject.isNull("lineHeight") ?
                    30 : jsonObject.getInt("lineHeight");
            String text = jsonObject.getString("text");
            alignment = alignment < 0 ? PosPrinter.TEXT_ALIGNMENT_LEFT : alignment;
            lineHeight = lineHeight <= 0 ? 30 : lineHeight;
            if (!TextUtils.isEmpty(text) &&
                    PrintUtils.getPrinter().printTexts(cordova.getActivity(), alignment, lineHeight, text)) {
                callbackContext.success();
            } else {
                callbackContext.error("error");
            }
        }
        return super.execute(action, args, callbackContext);

    }

}
