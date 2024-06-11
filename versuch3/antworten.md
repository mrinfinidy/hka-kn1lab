# Antworten

## Aufgabe 1
### 1.3 Ping Teammitglieder
Ja

mininet> pingall
*** Ping: testing ping reachability
ben -> X ela elias lisa lukas nas r1 X 
burak -> X X X X X nas X r2 
ela -> ben X elias lisa lukas nas r1 X 
elias -> ben X ela lisa lukas nas r1 X 
lisa -> ben X ela elias lukas nas r1 X 
lukas -> ben X ela elias lisa nas r1 X 
nas -> ben burak ela elias lisa lukas r1 r2 
r1 -> ben burak ela elias lisa lukas nas r2 
r2 -> X burak X X X X nas X 
*** Results: 30% dropped (50/72 received)
### 1.4 Ping Ben/NAS
mininet> ben ping -c 5 10.0.1.2
PING 10.0.1.2 (10.0.1.2) 56(84) bytes of data.
64 bytes from 10.0.1.2: icmp_seq=1 ttl=63 time=0.089 ms
64 bytes from 10.0.1.2: icmp_seq=2 ttl=63 time=0.117 ms
64 bytes from 10.0.1.2: icmp_seq=3 ttl=63 time=0.121 ms
64 bytes from 10.0.1.2: icmp_seq=4 ttl=63 time=0.059 ms
64 bytes from 10.0.1.2: icmp_seq=5 ttl=63 time=0.120 ms

--- 10.0.1.2 ping statistics ---
5 packets transmitted, 5 received, 0% packet loss, time 4082ms
rtt min/avg/max/mdev = 0.059/0.101/0.121/0.024 ms
### 1.5 Traceroute
#### Route: ben -> nas
mininet> ben traceroute -m 5 10.0.1.2
traceroute to 10.0.1.2 (10.0.1.2), 5 hops max, 60 byte packets
 1  10.0.0.1 (10.0.0.1)  0.102 ms  0.036 ms  0.030 ms
 2  10.0.1.2 (10.0.1.2)  0.056 ms  0.043 ms  0.040 ms
#### Route: burak-> nas 
mininet> burak traceroute -m 5 10.0.1.2
traceroute to 10.0.1.2 (10.0.1.2), 5 hops max, 60 byte packets
 1  10.0.2.1 (10.0.2.1)  0.164 ms  0.036 ms  0.030 ms
 2  10.0.1.64 (10.0.1.64)  0.057 ms  0.044 ms  0.039 ms
 3  10.0.1.2 (10.0.1.2)  0.062 ms  0.051 ms  0.051 ms