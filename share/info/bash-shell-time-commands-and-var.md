Don't forget that there is a difference between bash's builtin time (which
should be called by default when you do time command) and /usr/bin/time (which
should require you to call it by its full path).

The builtin time always prints to stderr, but /usr/bin/time will allow you to
send time's output to a specific file, so you do not interfere with the
executed command's stderr stream. Also, /usr/bin/time's format is configurable
on the command line or by the environment variable TIME, whereas bash's builtin
time format is only configured by the TIMEFORMAT environment variable.

$ time factor 1234567889234567891 # builtin
1234567889234567891: 142662263 8653780357

real    0m3.194s
user    0m1.596s
sys 0m0.004s
$ /usr/bin/time factor 1234567889234567891
1234567889234567891: 142662263 8653780357
1.54user 0.00system 0:02.69elapsed 57%CPU (0avgtext+0avgdata 0maxresident)k
0inputs+0outputs (0major+215minor)pagefaults 0swaps
$ /usr/bin/time -o timed factor 1234567889234567891 # log to file `timed`
1234567889234567891: 142662263 8653780357
$ cat timed
1.56user 0.02system 0:02.49elapsed 63%CPU (0avgtext+0avgdata 0maxresident)k
0inputs+0outputs (0major+217minor)pagefaults 0swaps
