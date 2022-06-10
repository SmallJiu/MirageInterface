# MirageInterface

This is a Minecraft mod

It add a block to remote link other block, it can transmission something to the other block.
usage:
  1. use a mirage connector to shift + right click want to link block, and, shift + right click the mirage interface, it will success to link. it can link other dimension block, but the chunk and block need load on world.
  2. after, you can right click interface some face to open a gui, it can put some capability type item to inerface, you need put some capability type item to mirage interface the click face to add transmission type. example: you use 'up' face to transmission forge energy, need put forge energy capability type item to mirage interface 'up' gui to add type.

How to custom transmission capability type:
  1. create 'capabilitys.json' file put to '.minecraft' folder. file content like this, the example file is in mod jar, you can see and use to create a new file
    
    {
     "main": [
        {
          // the capability class, see capabilitys.txt
          "handler": "net.minecraftforge.fluids.capability.IFluidHandlerItem",
          
          // the capability show name
          "name": "Item Fluid",
          
          // use the item to create recipe, JiuCore format
          "item": "minecraft:stone@1@1",
          
          // the capability type item show the texture
          "texture": "minecraft:items/barrier"
          
          /*
          "texture": {
		"layer0": "mi:items/capability/fluid",
		"layer1": "mi:items/capability/forge_energy",
		"layer...": "texture path"
	}
			
	"texture": [
		"mi:items/capability/fluid",
		"mi:items/capability/forge_energy",
		"texture path"
	]
          */
        }
     ]
    }

