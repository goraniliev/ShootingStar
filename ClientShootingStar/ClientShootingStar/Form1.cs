using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ClientShootingStar
{
    public partial class Form1 : Form
    {
        IPEndPoint serverAddress;
        Socket clientSocket;
        public Form1()
        {
            InitializeComponent();
            initConnection();
        }

        void initConnection()
        {
            serverAddress = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 4343);

            clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            clientSocket.Connect(serverAddress);
        }

        private void Red_Click(object sender, EventArgs e)
        {
            
            string response = Communication.sendColorNumber(clientSocket, "1");
            if (response == "-1")
            {
                //startingPosition();
                MessageBox.Show("Ball not found");
            } else
            activateRobot(response);
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string response = Communication.sendColorNumber(clientSocket, "2");
            if (response == "-1")
            {
                //startingPosition();
                MessageBox.Show("Ball not found");
            }
            else
                activateRobot(response);
        }

        private void button3_Click(object sender, EventArgs e)
        {
            string response = Communication.sendColorNumber(clientSocket, "3");
            if (response == "-1")
            {
                //startingPosition();
                MessageBox.Show("Ball not found");
            }
            else
                activateRobot(response);
        }

        //Viki
        private void kickingPosition()
        {
            serialPort1.Open();

            byte[] niza = new byte[3];

            niza[0] = 255;
            niza[1] = 3;
            niza[2] = angleToByte(0);

            serialPort1.Write(niza, 0, 3);
            
            niza[0] = 255;
            niza[1] = 2;
            niza[2] = angleToByte(40);

            serialPort1.Write(niza, 0, 3);

            niza[0] = 255;
            niza[1] = 1;
            niza[2] = angleToByte(20);

            serialPort1.Write(niza, 0, 3);
            
            serialPort1.Close();
        }

        byte angleToByte(int angle)
        {
            return (byte)(255 * angle / 120);
        }

        private void startingPosition()
        {
            kickingPosition();

            serialPort1.Open();

            byte[] niza = new byte[3];
            niza[0] = 255;
            niza[1] = 0;
            niza[2] = angleToByte(60);
            serialPort1.Write(niza, 0, 3);

            niza[0] = 255;
            niza[1] = 1;
            niza[2] = angleToByte(60);
            serialPort1.Write(niza, 0, 3);

            niza[0] = 255;
            niza[1] = 2;
            niza[2] = angleToByte(60);
            serialPort1.Write(niza, 0, 3);

            niza[0] = 255;
            niza[1] = 3;
            niza[2] = angleToByte(60);
            serialPort1.Write(niza, 0, 3);

            serialPort1.Close();

        }

        private void kick()
        {
            serialPort1.Open();

            byte[] niza = new byte[3];
            niza[0] = 255;
            niza[1] = 3;
            niza[2] = angleToByte(80);

            serialPort1.Write(niza, 0, 3);

            serialPort1.Close();
        }

        private void rotate(int angle)
        {
            serialPort1.Open();

            byte[] niza = new byte[3];
            niza[0] = 255;
            niza[1] = 0;
            niza[2] = angleToByte(angle);
            serialPort1.Write(niza, 0, 3);
            serialPort1.Close();
        }

        public void activateRobot(string angle)
        {
            int intAngle = Int32.Parse(angle);

            if (intAngle == -1)
            {
                startingPosition();
                return;
            }

            kickingPosition();

            rotate(intAngle);

            kick();

            kickingPosition();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
    
}
