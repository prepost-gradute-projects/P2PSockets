import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.io.File;
/**
 * Created by jeslev on 18/10/15.
 */
public class GUITable extends JFrame implements Observer{
    private JPanel mainPanel;
    private JPanel panelList;
    private JPanel panelDownload;
    private JTable peerTable;
    private JButton downloadButton;
    private JTable downloadTable;
    private JButton pauseButton;
    private JButton continueButton;
    private JButton cancelButton;
    private JButton clearButton;
    private JPanel buttonPanel;
    private JButton connectButton;
    private JButton disconnectButton;
    private JTextField portText;

    private static DownloadTable downloadModel;
    public static PeerTable peerModel;
    public static String port;

    private ClientP2P connection;
    public static ServerP2P server;

    public static FilesTable filesTable;
    public static PeerFileTable pft;

    public GUITable(){
        super("Peer to Peer  - Jesus Lovon");
        setContentPane(mainPanel);

        downloadModel = new DownloadTable();
        peerModel = new PeerTable();



        /** setting up peers table **/
        peerTable.setModel(peerModel);
        peerTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        tableSelectionChanged();
                    }
                }
        );
        peerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //for(int i=0;i<strs.length;i++) peerModel.addItem(new ItemList(strs[i],"1324"));

        panelList.setBorder(BorderFactory.createTitledBorder("Lista de Peers"));
        panelList.add(new JScrollPane(peerTable), BorderLayout.CENTER);

        /** setting up download table **/
        downloadTable.setModel(downloadModel);

        downloadTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                            @Override
                            public void valueChanged(ListSelectionEvent e) {
                                tableSelectionChanged();
                            }
                        });

        //just one selection at once
        downloadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        //Progress bar
        ProgressBar renderer = new ProgressBar(0,100);
        renderer.setStringPainted(true);
        downloadTable.setDefaultRenderer(JProgressBar.class, renderer);

        downloadTable.setRowHeight((int) renderer.getPreferredSize().getHeight());

        panelDownload.setBorder(BorderFactory.createTitledBorder("Descargas en curso"));
        panelDownload.add(new JScrollPane(downloadTable), BorderLayout.CENTER);

        /** setting up buttons panel **/
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ind = downloadTable.getSelectedRow();
                if(ind<0) return;
                Download tmp = downloadModel.getDownload(ind);
                tmp.pause();
            }
        });

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ind = downloadTable.getSelectedRow();
                if(ind<0) return;
                Download tmp = downloadModel.getDownload(ind);
                tmp.resume();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ind = downloadTable.getSelectedRow();
                if(ind<0) return;
                Download tmp = downloadModel.getDownload(ind);
                tmp.cancel();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ind = downloadTable.getSelectedRow();
                if(ind<0) return;
                downloadModel.clearDownload(ind);

            }
        });

        /* setting up disconnect and connect button*/
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                port = portText.getText().toString();
                /**List files **/
                File f = null;
                File[] paths;
                String[] nameFiles=null;
                try{
                    f = new File("/home/yarvis/"+port);
                    paths = f.listFiles();
                    nameFiles = new String[paths.length];
                    int pos = 0;
                    for(File path : paths){
                        String name[] =path.toString().split("/");
                        nameFiles[pos++] = name[name.length-1];
                    }
                }catch (Exception ex) {ex.printStackTrace();}

                pft = new PeerFileTable(nameFiles, "127.0.0.1",port);

                /*setting connection to server of address*/
                connection = new ClientP2P("127.0.0.1");
                connection.sendTable(pft);

                /*setting local server to connect to peers */
                server = new ServerP2P(port);

                JOptionPane.showMessageDialog(null, "Peer conectado");
                connection.start();
                server.start();
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connection.setStop();
                peerModel.clearTable();
                for(int i=0;i<downloadModel.getRowCount();i++){
                    Download tmp = downloadModel.getDownload(i);
                    int status  = tmp.getStatus();
                    if(status==Download.DOWNLOADING) tmp.pause();
                }
                server.setStop();
                JOptionPane.showMessageDialog(null, "Peer desconectado");
            }
        });

        /*add download button*/
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ind = peerTable.getSelectedRow();
                if (ind >= 0) {
                    ItemList tmp = peerModel.getitem(ind);
                    Download download = new Download(tmp.getIp(), tmp.getFile(), port);
                    downloadModel.addDownload(download);

                }else{
                    JOptionPane.showMessageDialog(null,"Seleccionar un archivo de la lista para la descarga");

                }
            }
        });
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    public void update(Observable o, Object arg){

    }

    public void tableSelectionChanged(){

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


    public static void resumeAll(){
        for(int i = 0; i<downloadModel.getRowCount();i++){
            Download tmp = downloadModel.getDownload(i);
            if(tmp.getStatus()==Download.PAUSED){
                tmp.resume();
            }
        }
    }
}
