require_relative 'XBeeResponse'

class ReceivePacket16Response < XBeeResponse
  attr_reader :source_addr, :rssi, :options, :data
  
  def initialize(payload, checksum)
    super(payload, checksum)
    parse_payload
    @api_id = 0x81
  end

  def to_s
    message = @data.map {|x| x.chr }
    message.join
  end

  private

  def parse_payload
    @source_addr = "#{@payload[0].to_s(2)}#{@payload[1].to_s(2)}".to_i(2)
    @rssi = @payload[2]
    @options = @payload[3]
    data_end = length - 1
    @data = @payload[4..data_end]
  end

end