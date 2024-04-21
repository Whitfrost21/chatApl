import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Server extends JFrame {

    ServerSocket server;
    Socket socket;
    BufferedReader read;
    PrintWriter write;
    private JLabel header = new JLabel("Server side");
    private JTextField mesgInput= new JTextField(20);
    private JTextArea mesgArea = new JTextArea();
    private Font font= new Font("Times New Roman",Font.BOLD,25);
    private JButton jbut = new JButton("Send");

    public Server(){
        try {
            server=new ServerSocket(5551);
            System.out.println("server is ready for connection ");
            System.out.println("waiting for response...");
            socket = server.accept();

            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            write = new PrintWriter(socket.getOutputStream());

            CreateGUI();
            handleIO();
            Readdata();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void CreateGUI()
{
        //design the swing
        this.setTitle("Server Side");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.decode("#888888"));

        //set fonts
        header.setFont(font);
        mesgInput.setFont(font);
        mesgArea.setFont(font);

        //set location of header
        ImageIcon icon = new ImageIcon("../assets/logo.png");
        int wid = 60, high = 60;
        Image im = icon.getImage().getScaledInstance(wid, high, Image.SCALE_SMOOTH);
        header.setIcon(new ImageIcon(im));
        header.setHorizontalTextPosition(SwingConstants.CENTER);
        header.setVerticalTextPosition(SwingConstants.BOTTOM);
        header.setHorizontalAlignment(SwingConstants.CENTER);

        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));



        //set area uneditable and move to center
        mesgArea.setEditable(false);
        mesgArea.requestFocus();
        mesgInput.setHorizontalAlignment(SwingConstants.CENTER);
        jbut.setHorizontalAlignment(SwingConstants.RIGHT);
        jbut.setSize(40,40);
        JPanel jp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        jp.add(mesgInput);
        jp.add(jbut);


        //frame
        this.setLayout(new BorderLayout());
        this.add(header, BorderLayout.NORTH);
        //add components
        this.add(header,BorderLayout.NORTH);
        JScrollPane js = new JScrollPane(mesgArea);
        Dimension viewportSize = js.getViewport().getSize();
        Dimension documentSize = mesgArea.getPreferredSize();
        int yPosition = documentSize.height - viewportSize.height;
        js.getViewport().setViewPosition(new Point(0, yPosition));
        this.add(js,BorderLayout.CENTER);
        this.add(jp,BorderLayout.SOUTH);


        this.setVisible(true);
    }

    private void handleIO(){
        jbut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a){

                startevent();
            }
        });
        mesgInput.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10)
            {
                    startevent();
                }}
        });

    }

    public void startevent() {
        String content = mesgInput.getText();
        if (content.equals("")) {
            JOptionPane.showMessageDialog(null, "cannot send empty message");

        } else {
            mesgArea.append("Me: " + content + "\n");
            write.println(content);
            write.flush();
            mesgInput.setText(null);
            mesgInput.requestFocus();
            if (content.equals("\\exit")) {
                try {
                    JOptionPane.showMessageDialog(null, "You ended this chat");
                    mesgInput.setEditable(false);
                    blockframe();
                    socket.close();
                } catch (Exception r) {
                    r.printStackTrace();
                }

            }
        }
    }

    public void  Readdata()
{
        Runnable r1=new Runnable() {
            @Override
            public void run()
        {

                System.out.println("reader started");

                try {

                    while (!socket.isClosed())
                {

                        String message = read.readLine();

                        if(message.equals("\\exit"))
                    {
                            JOptionPane.showMessageDialog(null,"Client terminated the chat");
                            mesgInput.setEnabled(false);
                            blockframe();
                            socket.close();
                            break;
                        }

                        mesgArea.append("Client: "+message+"\n");
                    }
                } catch (Exception e) {

                    System.out.println("connection terminated");
                }

            }
        };
        Thread run = new Thread(r1);
        run.start();
    }

    public void blockframe() {
        this.dispose();
    }

    public static void main(String[] args) {
        System.out.println("this is server...going to start server");
        new Server();
    }
}
