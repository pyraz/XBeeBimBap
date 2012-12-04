package com.trevorwhitney.ioio.domain;

import org.jruby.Ruby;
import org.jruby.RubyObject;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.RubyClass;


public class XBeeResponse extends RubyObject  {
    private static final Ruby __ruby__ = Ruby.getGlobalRuntime();
    private static final RubyClass __metaclass__;

    static {
        String source = new StringBuilder("require 'java'\n" +
            "java_package 'com.trevorwhitney.ioio.domain'\n" +
            "# @!attribute api_id [r]\n" +
            "#   @return [Integer] The API type ID of the packet.\n" +
            "# @!attribute payload[r]\n" +
            "#   @return [Array] An array of bytes representing the packet's data frame.\n" +
            "# @!attribute checksum[r]\n" +
            "#   @return [Integer] The checksum of the packet.\n" +
            "class XBeeResponse\n" +
            "  attr_reader :api_id, :payload, :checksum\n" +
            "  @api_id = 0\n" +
            "\n" +
            "  # @param [Array] payload An array of bytes representing the data\n" +
            "  #   frame of the packet.\n" +
            "  # @param [Integer] checksum The last byte of the response packet.\n" +
            "  def initialize(payload, checksum)\n" +
            "    @payload = payload\n" +
            "    @checksum = checksum\n" +
            "  end\n" +
            "\n" +
            "  \n" +
            "  # **Validates that the checksum is correct**: To verify checksum, \n" +
            "  # add up all bytes (excluding start delimeter and length, but\n" +
            "  # including api id and checksum), keep only the lowest 8 bits, if it \n" +
            "  # equals 0xFF, or 255, then the packet is valid.\n" +
            "  #\n" +
            "  java_signature 'boolean isValid()'\n" +
            "  def valid?\n" +
            "    value = @payload.inject {|sum,x| sum+x } + @checksum + @api_id\n" +
            "\n" +
            "    if value & 255 == 255\n" +
            "      true\n" +
            "    else\n" +
            "      false\n" +
            "    end\n" +
            "  end\n" +
            "\n" +
            "  # Returns the length (in bytes) of the payload.\n" +
            "  #\n" +
            "  # @return [Integer]\n" +
            "  def length\n" +
            "    @payload.size\n" +
            "  end\n" +
            "\n" +
            "end").toString();
        __ruby__.executeScript(source, "lib/XBeeResponse.rb");
        RubyClass metaclass = __ruby__.getClass("XBeeResponse");
        metaclass.setRubyStaticAllocator(XBeeResponse.class);
        if (metaclass == null) throw new NoClassDefFoundError("Could not load Ruby class: XBeeResponse");
        __metaclass__ = metaclass;
    }

    /**
     * Standard Ruby object constructor, for construction-from-Ruby purposes.
     * Generally not for user consumption.
     *
     * @param ruby The JRuby instance this object will belong to
     * @param metaclass The RubyClass representing the Ruby class of this object
     */
    private XBeeResponse(Ruby ruby, RubyClass metaclass) {
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
        return new XBeeResponse(ruby, metaClass);
    }

    
    public  XBeeResponse(Object payload, Object checksum) {
        this(__ruby__, __metaclass__);
        IRubyObject ruby_payload = JavaUtil.convertJavaToRuby(__ruby__, payload);
        IRubyObject ruby_checksum = JavaUtil.convertJavaToRuby(__ruby__, checksum);
        RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "initialize", ruby_payload, ruby_checksum);

    }

    
    public boolean isValid() {

        IRubyObject ruby_result = RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "valid?");
        return (Boolean)ruby_result.toJava(boolean.class);

    }

    
    public Object length() {

        IRubyObject ruby_result = RuntimeHelpers.invoke(__ruby__.getCurrentContext(), this, "length");
        return (Object)ruby_result.toJava(Object.class);

    }

}
