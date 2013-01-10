import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKey;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;
import com.sshtools.j2ssh.openssh.OpenSSHPrivateKeyFormat;
public class SSHToolsToOpenSSH {
  public static void main(String[] args) {
    try {

      BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in));

      String filename = new String("");
      if (args.length > 0) {
        filename = args[0];
      }
      else {

        System.out.print("Input private key file: ");
        filename = reader.readLine();
      }

      SshPrivateKeyFile file =
          SshPrivateKeyFile.parse(new File(filename));

      String passphrase = null;
      if (file.isPassphraseProtected()) {
        System.out.print("Enter passphrase? ");
        passphrase = reader.readLine();
      }

      SshPrivateKey key = file.toPrivateKey(passphrase);

      OpenSSHPrivateKeyFormat format = new OpenSSHPrivateKeyFormat();
      SshPrivateKeyFile newfile = SshPrivateKeyFile.create( key, passphrase, format);
      file.setFormat(format, passphrase);
      FileOutputStream out = new FileOutputStream("key.out");
      out.write(newfile.getBytes());
      out.close();
      System.out.println("Wrote new key to 'key.out'!");

    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
