package com.zoway.parkmanage.view;

import java.util.Date;

import com.zoway.parkmanage.utils.print.BasePrinter;
import com.zoway.parkmanage.utils.print.PrinterFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PrintActivity extends BaseActivity {

	protected BasePrinter basePrinter = PrinterFactory.getPrinter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		basePrinter.doBeforeCreate();
		super.onCreate(savedInstanceState);
		basePrinter.doAfterCreate();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		basePrinter.doBeforeStart();
		super.onStart();
		basePrinter.doAfterStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		basePrinter.doBeforeRestart();
		super.onRestart();
		basePrinter.doAfterRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		basePrinter.doBeforeResume();
		super.onResume();
		basePrinter.doAfterResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		basePrinter.doBeforePause();
		super.onPause();
		basePrinter.doAfterPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		basePrinter.doBeforeStop();
		super.onStop();
		basePrinter.doAfterStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		basePrinter.doBeforeDestory();
		super.onDestroy();
		basePrinter.doAfterDestory();
		this.basePrinter.printDia = null;
		this.basePrinter.printHandler = null;
		this.basePrinter.activity = null;
		this.basePrinter = null;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		basePrinter.doBeforeBackPressed();
		super.onBackPressed();
		basePrinter.doAfterBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		basePrinter.doBeforeActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
		basePrinter.doAfterActivityResult(requestCode, resultCode, data);
	}

	public void doPrint(String hphm, Date parktime, String recno, int fromWhere) {
		basePrinter.doPrint(hphm, parktime, recno, fromWhere);
	}

	public boolean beforePrint() {
		return true;
	}

	public boolean afterPrint() {
		return true;
	}

}
