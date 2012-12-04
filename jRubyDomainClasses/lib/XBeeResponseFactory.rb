require 'pry'
require 'XBeeResponse'
require 'ReceivePacket16Response'
require 'java'
java_package 'com.trevorwhitney.ioio.domain'

class XBeeResponseFactory

  class InvalidPacketError < StandardError
  end

  class InvalidApiIdError < StandardError
  end

  class InvalidPacketLengthError < StandardError
  end

  # Creates an XBeeResponse object from a recieved `packet`
  #
  # @param [Array] packet An array of decimal values representing the
  #   bytes in a recieved packet. Packet should start with 0x7e, and
  #   end with a valid checksum.
  #
  # @return [XBeeResponse] An object of type XBeeResponse.
  # 
  # @raise [InvalidApiIdError] if the API ID does not match a valid ID.
  def self.generate(packet)
    @packet = packet
    parse_packet
    case @api_id
    when 0x81
      return ReceivePacket16Response.new(@payload, @checksum)
    else
      raise InvalidApiIdError.new(
        "#{@api_id} is not a valid API Identifier")
    end
  end

  # Parses out the API ID, Length, Checksum, and Payload from a packet.
  #
  # @raise [InvalidPacketError] if the start delimeter is not 0x7e.
  # @raise [InvalidPacketLengthError] if the declared packet length is
  #   not equal to the actual size in bytes of the parsed out payload.
  def self.parse_packet
    if @packet[0] != 0x7e
      raise InvalidPacketError.new("Invalid start delimeter")
    end

    @length = @packet[1] * 256 + @packet[2]
    @api_id = @packet[3]
    @checksum = @packet[-1]

    #payload will be equal to the length minus 1 for the api_id, which
    #we already figured out
    @payload = Array.new(@length - 1)
    counter = 0
    @payload.each_index do |i|
      #payload starts at 5th byte
      index = 4 + i
      @payload[i] = @packet[index]
    end

    if @payload.include? nil
      raise InvalidPacketLengthError.new(
        "Declared payload length does not match actual payload length")
    end
  end

end