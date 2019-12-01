sudo echo "auto eth0" > /etc/networkk/interfaces
sudo echo "iface eht0 inet dhcp" > /etc/networkk/interfaces

sudo apt-get update
sudo apt-get upgrade
sudo apt-get install dnsmasq hostpad

sudo systemctl stop dnsmasq
sudo systemctl stop hostpad

sudo echo "interface wlan0" > /etc/dhcpcd.conf
sudo echo "static ip_address=192.168.2.1/24" > /etc/dhcpcd.conf
sudo echo "nohook wpa_supplicant" > /etc/dhcpcd.conf

sudo service dhcpcd restart

sudo mv /etc/dnsmasq.conf /etc/dnsmasq.conf.orig

sudo echo "interface=wlan0" > /etc/dnsmasq.conf
sudo echo "dhcp-range=192.168.2.2,192.168.2.20,255.255.255.0,24h" > /etc/dnsmasq.conf

sudo echo "interface=wlan0" > /etc/hostapd/hostapd.conf
sudoe echo "driver=nl80211" > /etc/hostapd/hostapd.conf
sudo echo "ssid=rpi3plus" > /etc/hostapd/hostapd.conf
sudo echo "hw_mode=g" > /etc/hostapd/hostapd.conf
sudo echo "channel=7" > /etc/hostapd/hostapd.conf
sudo echo "wmm_enabled=0" > /etc/hostapd/hostapd.conf
sudo echo "macaddr_acl=0" > /etc/hostapd/hostapd.conf
sudo echo "auth_algs=1" > /etc/hostapd/hostapd.conf
sudo echo "ignore_broadcast_ssid=0" > /etc/hostapd/hostapd.conf
sudo echo "wpa=2" > /etc/hostapd/hostapd.conf
sudo echo "wpa_passphrase=kpu1234" > /etc/hostapd/hostapd.conf
sudo echo "wpa_key_mgmt=WPA-PSK" > /etc/hostapd/hostapd.conf
sudo echo "wpa_pairwise=TKIP" > /etc/hostapd/hostapd.conf
sudo echo "rsn_pairwise=CCMP" > /etc/hostapd/hostapd.conf

sudo systemctl start hostapd
sudo systemctl start dnsmasq

sudo iptables -t nat -A  POSTROUTING -o eth0 -j MASQUERADE
sudo sh -c "iptables-save > /etc/iptables.ipv4.nat"

sudo iptables-restore < /etc/iptables.ipv4.nat
