package com.k.modechange.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * ログを出力するクラス
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
public class OutputLog {

    // クラス定数宣言
    /** プロジェクト名 */
    private static String PROJECTNAME = "Modechange";
    /** ログ出力ディレクトリ */
    private static String LOGDIR = Environment.getExternalStorageDirectory().getPath() + "/log/";
    /** ログファイル名 */
    private static String SDFILE = LOGDIR + PROJECTNAME + ".txt";

    /**
     * @param message
     *            出力するメッセージ
     */
    public void logD(String message) {
	Log.d(PROJECTNAME + " : " + getHed((new Throwable()).getStackTrace()), message);
    }

    /**
     * @param context
     *            呼び出し元のContext
     * @param message
     *            出力するメッセージ
     */
    public void logT(Context context, String message) {
	Log.d(PROJECTNAME + " : " + getHed((new Throwable()).getStackTrace()), message);
	Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * ログ出力(ファイル)
     * 
     * @param message
     */
    public void logF(String message) {
	Log.d(PROJECTNAME + " : " + getHed((new Throwable()).getStackTrace()), message);
	
	// ログファイルに出力
//	outPutFile(getDate() + " : " + getHed((new Throwable()).getStackTrace()) + " : " + message);

    }

    /**
     * @param message
     *            出力するメッセージ
     * @param e
     *            Exception
     */
    public void logE(String message, Exception e) {
	StackTraceElement[] stack = e.getStackTrace();
	
	String tag = PROJECTNAME +  getHed((new Throwable()).getStackTrace());
	
	String errMsg = message + " \n " 
        		+ e.getClass().getName() + " : " + e.getMessage()+ " \n "
        		+"\t"+  stack[stack.length-1];
	
	Log.e(tag, errMsg);
	
	// ログファイルに出力
//	outPutFile(errMsg);
    }

    /**
     * @param message
     *            出力するメッセージ
     */
    public void logE(String message) {
	Log.e(PROJECTNAME + " : \n" + getHed((new Throwable()).getStackTrace()), message);
    }

    /**
     * 呼び出し元のメソッド名/クラス名/行数を作成する
     * 
     * @param ste
     *            StackTraceElement
     * @return メソッド名/クラス名/行数
     */
    private String getHed(StackTraceElement[] ste) {
	return ste[1].getMethodName() + "(" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ") ";
    }

    /**
     * 日付時刻の取得
     * 
     * @return 日付時刻
     * */
    private static String getDate() {
	Date now = new Date();
	return (now.getYear() + 1900) + "/" + (now.getMonth() + 1) + "/" + now.getDate() + " " + now.getHours() + ":"
		+ now.getMinutes() + ":" + now.getSeconds();
    }

    /**
     * ファイルにログを出力する
     * 
     * @param message
     *            出力メッセージ
     */
    static public void outPutFile(String message) {

	BufferedWriter bw = null;

	try {
	    try {
		// フォルダ作成
		mkDir(new File(LOGDIR));
	    } catch (IOException e) {
		e.printStackTrace();
		return;
	    }
	    FileOutputStream file = new FileOutputStream(SDFILE, true);
	    bw = new BufferedWriter(new OutputStreamWriter(file, "UTF-8"));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	try {

	    bw.append(message + "\n");

	} catch (IOException e) {
	    e.printStackTrace();
	}
	try {
	    bw.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	bw = null;
    }

    /**
     * @param dir
     *            作成するディレクトリ
     * @return 結果
     * @throws IOException
     */
    public static boolean mkDir(File dir) throws IOException {
	if (!dir.exists()) {
	    if (!dir.mkdirs()) {
		throw new IOException("File.mkdirs() failed.");
	    }
	    return true;
	} else if (!dir.isDirectory()) {
	    throw new IOException("Cannot create path. " + dir.toString() + " already exists and is not a directory.");
	} else {
	    return false;
	}
    }

    // public static boolean mkDirS(String dir) throws IOException {
    // return mkDirF(new File(dir));
    // }

}
