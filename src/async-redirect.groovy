
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector


def datagramSocket = new DatagramSocket();


def loc = InetAddress.getByName("127.0.0.1")

// 打开一个UDP Channel
def channel = DatagramChannel.open()

// 设定为非阻塞通道
channel.configureBlocking(false)
// 绑定端口
channel.socket().bind(new InetSocketAddress(3000))

// 打开一个选择器
def selector = Selector.open()
channel.register(selector, SelectionKey.OP_READ)

def byteBuffer = ByteBuffer.allocate(65536)


def oldTime = System.currentTimeMillis()
int frame = 0


final int count = 1000



while (true) {
    long delta = System.currentTimeMillis() - oldTime;
    if (delta >= 1000) {
        println(frame * 1000 / delta)
        frame = 0
        oldTime = System.currentTimeMillis() / 1000 * 1000

    }

    if (frame >= count) {
        Thread.yield()
        continue
    }
    frame++

    // 进行选择
    int n = selector.selectNow();
    if (n > 0) {
        // 获取以选择的键的集合
        Iterator iterator = selector.selectedKeys().iterator();

        while (iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();

            // 必须手动删除
            iterator.remove();

            if (key.isReadable()) {
                DatagramChannel datagramChannel = key.channel();

                byteBuffer.clear();
                // 读取
                datagramChannel.receive(byteBuffer);

                String receive = new String(byteBuffer.array(), 0, byteBuffer.position())
//                System.out.println(receive)
                break

            }
        }

    }
    datagramSocket.send(new DatagramPacket(byteBuffer.array(), byteBuffer.position(), loc, 10514));
}


