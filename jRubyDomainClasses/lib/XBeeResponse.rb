require 'java'
java_package 'com.trevorwhitney.ioio.domain'
# @!attribute api_id [r]
#   @return [Integer] The API type ID of the packet.
# @!attribute payload[r]
#   @return [Array] An array of bytes representing the packet's data frame.
# @!attribute checksum[r]
#   @return [Integer] The checksum of the packet.
class XBeeResponse
  attr_reader :api_id, :payload, :checksum
  @api_id = 0

  # @param [Array] payload An array of bytes representing the data
  #   frame of the packet.
  # @param [Integer] checksum The last byte of the response packet.
  def initialize(payload, checksum)
    @payload = payload
    @checksum = checksum
  end

  
  # **Validates that the checksum is correct**: To verify checksum, 
  # add up all bytes (excluding start delimeter and length, but
  # including api id and checksum), keep only the lowest 8 bits, if it 
  # equals 0xFF, or 255, then the packet is valid.
  #
  java_signature 'boolean isValid()'
  def valid?
    value = @payload.inject {|sum,x| sum+x } + @checksum + @api_id

    if value & 255 == 255
      true
    else
      false
    end
  end

  # Returns the length (in bytes) of the payload.
  #
  # @return [Integer]
  def length
    @payload.size
  end

  java_signature 'String toString()'
  def to_s
    super
  end

end