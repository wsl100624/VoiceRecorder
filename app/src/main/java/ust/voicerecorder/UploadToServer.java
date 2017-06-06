package ust.voicerecorder;

import android.os.AsyncTask;

import com.jcraft.jsch.*;

import java.util.Properties;


public class UploadToServer extends AsyncTask<Void, Void, Void>
{

    protected Void doInBackground(Void... params) {
        try {
            executeRemoteCommand("willwang3027", "wsl8245590", "140.209.68.84", 22);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void executeRemoteCommand(String username, String password, String hostname, int port) {
        try {
            JSch ssh = new JSch();
            Session session = ssh.getSession(username, hostname, port);
            session.setPassword(password);


            // Avoid asking for key confirmation
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            // SSH Channel
            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;


            String directory = "/home/willwang3027";
            sftp.cd(directory);
            sftp.put("/storage/emulated/0/voices/audio.3gp", directory);

            Boolean success = true;

            if(success){
                System.out.println("Upload Successfully");
            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            System.out.println("JSchException e "+ e.getMessage().toString());
            e.printStackTrace();
        } catch (SftpException e) {
            System.out.println("SftpException e "+ e.getMessage().toString());
            e.printStackTrace();
        }
    }

}
