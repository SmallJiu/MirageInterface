# MirageInterface

This is a Minecraft mod

It add a block to remote link other block, it can transmission something to the other block.
usage:
  1. use a mirage connector to shift + right click want to link block, and, shift + right click the mirage interface, it will success to link. it can link other dimension block, but the chunk and block need load on world.
  2. after, you can right click interface some face to open a gui, it can put some capability type item to inerface, you need put some capability type item to mirage interface the click face to add transmission type. example: you use 'up' face to transmission forge energy, need put forge energy capability type item to mirage interface 'up' gui to add type.

How to custom transmission capability type:
  1. create 'capabilitys.json' file put to '.minecraft' folder. file content like this:
    
    {
     "main": [
        {
          "handler": "net.minecraftforge.fluids.capability.IFluidHandlerItem",
          "name": "Item Fluid",
          "item": "minecraft:stone@1@1",
          "texture": "minecraft:items/barrier"
        }
     ]
    }
    
    handler: the capability class, see capabilitys.txt
    name: the capability show name
    item: use the item to create recipe
    texture: the capability type item show the texture

