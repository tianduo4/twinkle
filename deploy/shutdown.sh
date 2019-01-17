ps_pid=`ps -ef|grep twinkle.jar|grep -v grep|awk '{print $2}'`
kill -9 ${ps_pid}
echo "twinkle shutdown ok"
