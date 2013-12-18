package com.uminho.uce15.cityroots;

/**
 * Created by fabiocorreia on 18/12/13.
 */
public class PlusClient {
    private static PlusClient ourInstance = new PlusClient();

    public static PlusClient getInstance() {
        return ourInstance;
    }

    private PlusClient() {
    }
}
