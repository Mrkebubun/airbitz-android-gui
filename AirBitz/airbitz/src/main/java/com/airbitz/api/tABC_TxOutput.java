/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.airbitz.api;

public class tABC_TxOutput {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected tABC_TxOutput(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(tABC_TxOutput obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        coreJNI.delete_tABC_TxOutput(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setInput(boolean value) {
    coreJNI.tABC_TxOutput_input_set(swigCPtr, this, value);
  }

  public boolean getInput() {
    return coreJNI.tABC_TxOutput_input_get(swigCPtr, this);
  }

  public void setValue(SWIGTYPE_p_int64_t value) {
    coreJNI.tABC_TxOutput_value_set(swigCPtr, this, SWIGTYPE_p_int64_t.getCPtr(value));
  }

  public SWIGTYPE_p_int64_t getValue() {
    return new SWIGTYPE_p_int64_t(coreJNI.tABC_TxOutput_value_get(swigCPtr, this), true);
  }

  public void setSzAddress(String value) {
    coreJNI.tABC_TxOutput_szAddress_set(swigCPtr, this, value);
  }

  public String getSzAddress() {
    return coreJNI.tABC_TxOutput_szAddress_get(swigCPtr, this);
  }

  public void setSzTxId(String value) {
    coreJNI.tABC_TxOutput_szTxId_set(swigCPtr, this, value);
  }

  public String getSzTxId() {
    return coreJNI.tABC_TxOutput_szTxId_get(swigCPtr, this);
  }

  public void setIndex(SWIGTYPE_p_int64_t value) {
    coreJNI.tABC_TxOutput_index_set(swigCPtr, this, SWIGTYPE_p_int64_t.getCPtr(value));
  }

  public SWIGTYPE_p_int64_t getIndex() {
    return new SWIGTYPE_p_int64_t(coreJNI.tABC_TxOutput_index_get(swigCPtr, this), true);
  }

  public tABC_TxOutput() {
    this(coreJNI.new_tABC_TxOutput(), true);
  }

}
