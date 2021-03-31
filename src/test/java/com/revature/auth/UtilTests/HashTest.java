package com.revature.auth.UtilTests;

import com.revature.auth.utils.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashTest {

    @Test
    void hash(){
        String text = "Hello, world!";
        String hash_text = HashUtil.hash(text);
        Assertions.assertNotNull(hash_text);
        Assertions.assertNotEquals(text, hash_text);

        String text2 = "Goodbye, world.";
        String hash_text2 = HashUtil.hash(text2);
        Assertions.assertNotEquals(hash_text, hash_text2);

        System.out.println(hash_text);
    }
}
