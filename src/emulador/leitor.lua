-- responsável por enviar as infos de vida e timer pra um programa intermediário Java
-- onde esse script se conecta com ele pra esse envio
-- monitorando a memória RAM pra pegar esses dados

memory.usememorydomain("WRAM")

local VIDA_P1, VIDA_P2 = 0x0C2B, 0x0E2B   -- byte alto da vida
local RAW_P1, RAW_P2   = 0x0C2A, 0x0E2A   -- 2 bytes, para conferir
local TIMER = 0x1AC8
local VIDA_MAX = 176

local function vida(addr)
  local v = memory.read_u8(addr)
  if v > VIDA_MAX then return 0 end       -- passou de 0, KO
  return v
end

local function timer_bcd(addr)
  local b = memory.read_u8(addr)
  return math.floor(b / 16) * 10 + (b % 16)
end

local msg = ""
while true do
  msg = "P1=" .. vida(0x0C2B) .. ";P2=" .. vida(0x0E2B) .. ";T=" .. timer_bcd(0x1AC8)
  comm.socketServerSend(msg .. "\n")
  emu.frameadvance()
end