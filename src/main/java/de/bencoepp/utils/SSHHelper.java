package de.bencoepp.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import de.bencoepp.entity.Remote;

import java.io.ByteArrayOutputStream;

public class SSHHelper {
    public static boolean testConnection(Remote remote) throws JSchException, InterruptedException {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession(remote.getUser(), remote.getIp(), remote.getPort());
            session.setPassword(remote.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("ls");
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = new String(responseStream.toByteArray());
            if(responseString.isBlank()){
                return false;
            }
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return true;
    }

    public static String executeCommands(Remote remote, String[] commands) throws JSchException, InterruptedException {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession(remote.getUser(), remote.getIp(), remote.getPort());
            session.setPassword(remote.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            for (String command : commands) {
                channel.setCommand(command);
            }
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            return new String(responseStream.toByteArray());
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
}
