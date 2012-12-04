require 'XBeeResponse'
require 'java'
java_package 'com.trevorwhitney.ioio.domain'

class ReceivePacket16Response < XBeeResponse
  attr_reader :source_addr, :rssi, :options, :data
  
  def initialize(payload, checksum)
    super(payload, checksum)
    parse_payload
    @api_id = 0x81
  end

  java_signature 'String toString()'
  def to_s
    message = @data.map {|x| x.chr }
    message.join
  end

  private

  def parse_payload
    @source_addr = @payload[0] * 256 + @payload[1]
    @rssi = @payload[2]
    @options = @payload[3]
    data_end = length - 1
    @data = @payload[4..data_end]
  end

end