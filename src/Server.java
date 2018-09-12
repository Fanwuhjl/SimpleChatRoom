import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    //定义端口，客户端和服务端
    int port;
    List<Socket> clients;
    ServerSocket server;

    public static void main(String[] args) {
        new Server();
    }

    public Server(){
        try {
            port=8080;
            clients=new ArrayList<Socket>();
            server=new ServerSocket(port);

            //死循环，保持监听
            //启动一个客户端
            while (true){
                //ServerSocket一旦检测到有客户端接入，accept()方法就会马上返回一个socket对象与客户端的socket对接起来
                Socket socket=server.accept();
                clients.add(socket);

//                if (server.isClosed()){
//                    System.out.println("有人下线");
//                }
                //启动一个新的线程
                Mythread mythread=new Mythread(socket);
                mythread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //创建一个线程
    class Mythread extends Thread{
        Socket socket;
        BufferedReader br;
        PrintWriter pw;
        String msg;

        //初始化socket
        public Mythread(Socket socket){
            this.socket=socket;
        }

        @Override
        public void run() {
            try {
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                msg="欢迎"+socket.getInetAddress()+"进入聊天室，当前有"+clients.size()+"人";

                sendMsg();
                while((msg=br.readLine())!=null){
                    msg="【" + socket.getInetAddress() + "】说：" + msg;
                    sendMsg();
                }

            }catch (Exception e){
                clients.remove(this.socket);
                System.out.println("有人下线");
                System.out.println("当前有"+clients.size()+"人");
            }
        }

        private void sendMsg() {

            try{
                System.out.println(msg);

                for (int i = clients.size()-1; i >=0 ; i--) {
                    pw=new PrintWriter(clients.get(i).getOutputStream(),true);
                    pw.println(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
