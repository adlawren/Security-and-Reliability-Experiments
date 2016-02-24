//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

class InfiniteThread extends Thread {

  public void run(){

		try{
			while (true) {}
		}

		catch (ThreadDeath td){

      System.out.println("Insert empassioned faithlessness for the future of humanity here.");
			throw new ThreadDeath();
		}
	}
}
