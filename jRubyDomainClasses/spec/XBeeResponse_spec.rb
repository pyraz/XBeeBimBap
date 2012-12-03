require 'spec_helper'
require 'XBeeResponseFactory'
require 'ReceivePacket16Response'

describe XBeeResponse do

  before :all do
    @packet = [126, 0, 19, 129, 0, 2, 23, 0, 72, 101, 108, 108, 111, 
      32, 102, 114, 111, 109, 32, 50, 13, 10, 52]
    @response = XBeeResponseFactory.generate(@packet)
  end

  it "should have an API identifier" do
    @response.api_id.should == 0x81
  end

  it "should have a length equal to the size of the payload" do
    @response.length.should be > 0
    #payload length is one less than declared length, since the first
    #byte is for the api id
    @response.length.should be 18
  end

  it "should have a payload with the correct size" do
    @response.payload.length.should be == @response.length
  end

  it "should have a valid checksum" do
    @response.checksum.should be > 0
  end

  it "should verify the checksum" do
    @response.should be_valid
  end

  #unhappy paths
  it "should raise error on invalid packet" do
    bad_packet = @packet.clone
    bad_packet[0] = 13
    expect {XBeeResponseFactory.generate(bad_packet)}.to raise_error(
      XBeeResponseFactory::InvalidPacketError)
  end

  it "should catch an invalid checksum" do
    packet = @packet.clone
    packet[-1] = 13
    response = XBeeResponseFactory.generate(packet)
    response.should_not be_valid
  end

  it "should raise an error when the packet is the wrong size" do
    bad_packet = [126, 0, 19, 129, 0, 2, 23, 0, 108, 111, 
      32, 102, 114, 111, 109, 32, 50, 13, 10, 52]
    expect {XBeeResponseFactory.generate(bad_packet)}.to raise_error(
      XBeeResponseFactory::InvalidPacketLengthError)
  end

end