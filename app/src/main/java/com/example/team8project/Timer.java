package com.example.team8project;

public class Timer  implements java.lang.Runnable{

        @Override
        public void run() {
            int i = 10;
            while (i>0){
                try {
                    i--;
                    Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
                }
                catch (InterruptedException e) {

                }
            }
        }



    }

