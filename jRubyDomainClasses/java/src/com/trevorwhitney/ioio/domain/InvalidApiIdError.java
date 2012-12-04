package com.trevorwhitney.ioio.domain;

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;


public class InvalidApiIdError extends RubyObject  {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("require 'pry'\n" +
            "require 'XBeeResponse'\n" +
            "require 'ReceivePacket16Response'\n" +
            "require 'java'\n" +
            "java_package 'com.trevorwhitney.ioio.domain'\n" +
            "\n" +
            "class XBeeResponseFactory\n" +
            "\n" +
            "  class InvalidPacketError < StandardError\n" +
            "  end\n" +
            "\n" +
            "  class InvalidApiIdError < StandardError\n" +
            "  end\n" +
            "\n" +
            "  class InvalidPacketLengthError < StandardError\n" +
            "  end\n" +
            "\n" +
            "  # Creates an XBeeResponse object from a recieved `packet`\n" +
            "  #\n" +
            "  # @param [Array] packet An array of decimal values representing the\n" +
            "  #   bytes in a recieved packet. Packet should start with 0x7e, and\n" +
            "  #   end with a valid checksum.\n" +
            "  #\n" +
            "  # @return [XBeeResponse] An object of type XBeeResponse.\n" +
            "  # \n" +
            "  # @raise [InvalidApiIdError] if the API ID does not match a valid ID.\n" +
            "  def self.generate(packet)\n" +
            "    @packet = packet\n" +
            "    parse_packet\n" +
            "    case @api_id\n" +
            "    when 0x81\n" +
            "      return ReceivePacket16Response.new(@payload, @checksum)\n" +
            "    else\n" +
            "      raise InvalidApiIdError.new(\n" +
            "        \"#{@api_id} is not a valid API Identifier\")\n" +
            "    end\n" +
            "  end\n" +
            "\n" +
            "  # Parses out the API ID, Length, Checksum, and Payload from a packet.\n" +
            "  #\n" +
            "  # @raise [InvalidPacketError] if the start delimeter is not 0x7e.\n" +
            "  # @raise [InvalidPacketLengthError] if the declared packet length is\n" +
            "  #   not equal to the actual size in bytes of the parsed out payload.\n" +
            "  def self.parse_packet\n" +
            "    if @packet[0] != 0x7e\n" +
            "      raise InvalidPacketError.new(\"Invalid start delimeter\")\n" +
            "    end\n" +
            "\n" +
            "    @length = @packet[1] * 256 + @packet[2]\n" +
            "    @api_id = @packet[3]\n" +
            "    @checksum = @packet[-1]\n" +
            "\n" +
            "    #payload will be equal to the length minus 1 for the api_id, which\n" +
            "    #we already figured out\n" +
            "    @payload = Array.new(@length - 1)\n" +
            "    counter = 0\n" +
            "    @payload.each_index do |i|\n" +
            "      #payload starts at 5th byte\n" +
            "      index = 4 + i\n" +
            "      @payload[i] = @packet[index]\n" +
            "    end\n" +
            "\n" +
            "    if @payload.include? nil\n" +
            "      raise InvalidPacketLengthError.new(\n" +
            "        \"Declared payload length does not match actual payload length\")\n" +
            "    end\n" +
            "  end\n" +
            "\n" +
            "end").toString();
        __ruby__.executeScript(source, "lib/XBeeResponseFactory.rb");
        RubyClass metaclass = __ruby__.getClass("InvalidApiIdError");
        metaclass.setRubyStaticAllocator(InvalidApiIdError.class);
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: InvalidApiIdError");
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private InvalidApiIdError(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * A static method used by JRuby for allocating instances of this object
     * from Ruby. Generally not for user comsumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public static IRubyObject __allocate__(Ruby ruby, RubyClass metaClass) {
        return new InvalidApiIdError(ruby, metaClass);
    }
        
    /**
     * Default constructor. Invokes this(Ruby, RubyClass) with the classloader-static
     * Ruby and RubyClass instances assocated with this class, and then invokes the
     * no-argument 'initialize' method in Ruby.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    public InvalidApiIdError() {
        this(__ruby__, __metaclass__);
        RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "initialize");
    }


}
