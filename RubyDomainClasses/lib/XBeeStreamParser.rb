require_relative 'XBeeResponseFactory'
require 'serialport'
require 'pry'

port_str = "/dev/ttyUSB0"
baud = 57600
data_bits = 8
stop_bits = 1
parity = SerialPort::NONE

puts "Connecting Serial"

sp = SerialPort.new(port_str, baud, data_bits, stop_bits, parity)

while true
  b = sp.getbyte
  if b == 0x7e
    data = []
    data << b
    s = []
    b = sp.getbyte
    data << b
    s << b.to_s(2)
    b = sp.getbyte
    data << b
    s << b.to_s(2)
    @size = s.join.to_i(2)
    while @size > 0
      b = sp.getbyte
      data << b if b != nil
      @size -= 1
    end
    puts "#{data}"
  end
end