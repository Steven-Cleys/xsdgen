package be.devoowi.controller;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.jasypt.properties.PropertyValueEncryptionUtils;

import javax.faces.bean.ManagedBean;

/**
 * Created by cleysst on 11/10/2016.
 */
@ManagedBean
public class JasyptEncryptorView {
    private String input;
    private String password;
    private String output;

    public JasyptEncryptorView() {
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void genEncryption() {
        SimplePBEConfig config = new SimplePBEConfig();
        config.setKeyObtentionIterations(1000);
        config.setPassword(getPassword());
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        encryptor.initialize();
        setOutput(PropertyValueEncryptionUtils.encrypt(getInput(), encryptor));
        System.out.println("sdfsdf" + getOutput());
    }

    public void genDecryption() {

        SimplePBEConfig config = new SimplePBEConfig();
        config.setKeyObtentionIterations(1000);
        config.setPassword(getPassword());
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        encryptor.initialize();
        setOutput(PropertyValueEncryptionUtils.decrypt(getInput(), encryptor));
        System.out.println(getOutput());
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
