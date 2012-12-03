require 'spec_helper'
require 'ReceivePacket16Response'
require 'XBeeResponseFactory'

describe ReceivePacket16Response do
  before :all do
    @packet = [126, 0, 19, 129, 0, 2, 23, 0, 72, 101, 108, 108, 111, 
      32, 102, 114, 111, 109, 32, 50, 13, 10, 52]
    @response = XBeeResponseFactory.generate(@packet)
  end

  it "should be a ReceivePacket16Response" do
    @response.should be_a ReceivePacket16Response
  end

  it "should have an RSSI" do
    @response.rssi.should be 23
  end

  it "should have correct source address" do
    @response.source_addr.should == 2
  end

  it "should have correct options" do
    @response.options.should == 0
  end

  it "should have corrent amount of data" do
    @response.data.size.should == 14
  end

  it "should have correct serial data" do
    @response.to_s.should == "Hello from 2\r\n"
  end
end