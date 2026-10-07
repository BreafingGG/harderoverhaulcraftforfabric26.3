import json, os, shutil
R='src/main/resources'; NS='harderoverhaulcraft'
def w(p,o):
    p=os.path.join(R,p); os.makedirs(os.path.dirname(p),exist_ok=True); json.dump(o,open(p,'w'),indent=2)
TIERS={'gold_reinforced_iron':('iron','minecraft:gold_ingot'),'diamond_plus':('diamond','minecraft:gold_block'),'netherite_plus':('netherite','minecraft:gold_block')}
TOOLS=['pickaxe','axe','sword','shovel','hoe']
A=f'assets/{NS}'
# items
for it in ['diamond_nugget','rock_shard']:
    w(f'{A}/models/item/{it}.json',{'parent':'minecraft:item/generated','textures':{'layer0':f'{NS}:item/{it}'}})
    w(f'{A}/items/{it}.json',{'model':{'type':'minecraft:model','model':f'{NS}:item/{it}'}})
for t,(base,mat) in TIERS.items():
    for tool in TOOLS:
        n=f'{t}_{tool}'
        w(f'{A}/models/item/{n}.json',{'parent':'minecraft:item/handheld','textures':{'layer0':f'{NS}:item/{n}'}})
        w(f'{A}/items/{n}.json',{'model':{'type':'minecraft:model','model':f'{NS}:item/{n}'}})
        w(f'data/{NS}/recipe/{n}.json',{'type':'minecraft:crafting_transmute','category':'equipment','input':f'minecraft:{base}_{tool}','material':mat,'result':{'id':f'{NS}:{n}'}})
# blocks
for b in ['compressed_cobblestone','rock_shaper','crude_beacon','refined_beacon']:
    if b=='compressed_cobblestone': m={'parent':'minecraft:block/cube_all','textures':{'all':f'{NS}:block/{b}'}}
    else: m={'parent':'minecraft:block/cube_bottom_top','textures':{s:f'{NS}:block/{b}_{s}' for s in ['top','side','bottom']}}
    w(f'{A}/models/block/{b}.json',m)
    w(f'{A}/blockstates/{b}.json',{'variants':{'':{'model':f'{NS}:block/{b}'}}})
    w(f'{A}/items/{b}.json',{'model':{'type':'minecraft:model','model':f'{NS}:block/{b}'}})
    w(f'data/{NS}/loot_table/blocks/{b}.json',{'type':'minecraft:block','pools':[{'rolls':1,'entries':[{'type':'minecraft:item','name':f'{NS}:{b}'}],'conditions':[{'condition':'minecraft:survives_explosion'}]}],'random_sequence':f'{NS}:blocks/{b}'})
lang={'itemGroup.harderoverhaulcraft.main':'HarderOverhaulCraft','item.harderoverhaulcraft.diamond_nugget':'Diamond Nugget','item.harderoverhaulcraft.rock_shard':'Rock Shard',
 'block.harderoverhaulcraft.compressed_cobblestone':'Compressed Cobblestone','block.harderoverhaulcraft.rock_shaper':'Rock Shaper','block.harderoverhaulcraft.crude_beacon':'Crude Beacon','block.harderoverhaulcraft.refined_beacon':'Refined Beacon',
 'container.harderoverhaulcraft.rock_shaper':'Rock Shaper','gui.harderoverhaulcraft.lava':'Lava: %s/%s','gui.harderoverhaulcraft.mode_lava':'Lava mode','gui.harderoverhaulcraft.mode_basic':'Basic mode',
 'gui.harderoverhaulcraft.range':'Range: %s blocks','gui.harderoverhaulcraft.no_base':'No valid base (3x3 below)','jei.harderoverhaulcraft.rock_shaper':'Rock Shaper','jei.harderoverhaulcraft.lava_mode':'Lava (64 ops/bucket)','jei.harderoverhaulcraft.basic_mode':'Basic','jei.harderoverhaulcraft.seconds':'%ss'}
TN={'gold_reinforced_iron':'Gold-Reinforced Iron','diamond_plus':'Diamond+','netherite_plus':'Netherite+'}
for t in TIERS:
    for tool in TOOLS: lang[f'item.{NS}.{t}_{tool}']=f'{TN[t]} {tool.capitalize()}'
w(f'{A}/lang/en_us.json',lang)
# recipes
D='data/minecraft/recipe'
def smelt(name,ing,res,typ,xp):
    w(f'{D}/{name}.json',{'type':f'minecraft:{typ}','category':'misc','cookingtime':200 if typ=='smelting' else 100,'experience':xp,'ingredient':ing,'result':{'id':res}})
ores={'iron':(['iron_ore','deepslate_iron_ore','raw_iron'],'iron_nugget'),'gold':(['gold_ore','deepslate_gold_ore','raw_gold','nether_gold_ore'],'gold_nugget'),'copper':(['copper_ore','deepslate_copper_ore','raw_copper'],'copper_nugget')}
for m,(srcs,nug) in ores.items():
    for s in srcs:
        for typ in ['smelting','blasting']: smelt(f'{m}_ingot_from_{typ}_{s}',f'minecraft:{s}',f'minecraft:{nug}',typ,0.1)
for s in ['diamond_ore','deepslate_diamond_ore']:
    for typ in ['smelting','blasting']: smelt(f'diamond_from_{typ}_{s}',f'minecraft:{s}',f'{NS}:diamond_nugget',typ,0.25)
pat={'pickaxe':['XXX',' # ',' # '],'axe':['XX','X#',' #'],'sword':['X','X','#'],'shovel':['X','#','#'],'hoe':['XX',' #',' #'],'spear':['  X',' # ','#  ']}
for tool,p in pat.items():
    w(f'{D}/stone_{tool}.json',{'type':'minecraft:crafting_shaped','category':'equipment','key':{'#':'minecraft:stick','X':f'{NS}:rock_shard'},'pattern':p,'result':{'id':f'minecraft:stone_{tool}'}})
N=f'data/{NS}/recipe'
w(f'{N}/diamond_from_nuggets.json',{'type':'minecraft:crafting_shaped','category':'misc','key':{'#':f'{NS}:diamond_nugget'},'pattern':['##','##'],'result':{'id':'minecraft:diamond'}})
w(f'{N}/compressed_cobblestone.json',{'type':'minecraft:crafting_shaped','category':'building','key':{'#':'minecraft:cobblestone'},'pattern':['###','###','###'],'result':{'id':f'{NS}:compressed_cobblestone'}})
w(f'{N}/cobblestone_from_compressed.json',{'type':'minecraft:crafting_shapeless','category':'building','ingredients':[f'{NS}:compressed_cobblestone'],'result':{'id':'minecraft:cobblestone','count':9}})
w(f'{N}/rock_shaper.json',{'type':'minecraft:crafting_shaped','category':'misc','key':{'#':f'{NS}:compressed_cobblestone','C':'minecraft:crafting_table'},'pattern':['###','#C#','###'],'result':{'id':f'{NS}:rock_shaper'}})
w(f'{N}/crude_beacon.json',{'type':'minecraft:crafting_shaped','category':'misc','key':{'G':'minecraft:glass','I':'minecraft:iron_block','L':'minecraft:glowstone'},'pattern':['GGG','GIG','ILI'],'result':{'id':f'{NS}:crude_beacon'}})
w(f'{N}/refined_beacon.json',{'type':'minecraft:crafting_shaped','category':'misc','key':{'G':'minecraft:gold_block','B':f'{NS}:crude_beacon'},'pattern':['GGG','GBG','GGG'],'result':{'id':f'{NS}:refined_beacon'}})
# loot tables
def ore(block,item,mn=None,mx=None):
    fns=[]
    if mn: fns.append({'function':'minecraft:set_count','count':{'type':'minecraft:uniform','min':mn,'max':mx},'add':False})
    fns+= [{'function':'minecraft:apply_bonus','enchantment':'minecraft:fortune','formula':'minecraft:ore_drops'},{'function':'minecraft:explosion_decay'}]
    w(f'data/minecraft/loot_table/blocks/{block}.json',{'type':'minecraft:block','pools':[{'bonus_rolls':0.0,'rolls':1.0,'entries':[{'type':'minecraft:alternatives','children':[
      {'type':'minecraft:item','condition':'minecraft:tool/can_silk_touch','name':f'minecraft:{block}'},
      {'type':'minecraft:item','name':item,'functions':fns}]}]}],'random_sequence':f'minecraft:blocks/{block}'})
for p in ['','deepslate_']:
    ore(p+'iron_ore','minecraft:iron_nugget',2,4); ore(p+'gold_ore','minecraft:gold_nugget',2,4)
    ore(p+'copper_ore','minecraft:copper_nugget',2,5); ore(p+'diamond_ore',f'{NS}:diamond_nugget',1,2)
# tags
T='data/minecraft/tags'
w(f'{T}/block/mineable/pickaxe.json',{'values':[f'{NS}:{b}' for b in ['compressed_cobblestone','rock_shaper','crude_beacon','refined_beacon']]})
w(f'{T}/block/incorrect_for_iron_tool.json',{'values':['minecraft:diamond_ore','minecraft:deepslate_diamond_ore']})
for tool in TOOLS:
    w(f'{T}/item/{tool if tool!="axe" else "axe"}s.json',{'values':[f'{NS}:{t}_{tool}' for t in TIERS]})
w(f'data/{NS}/tags/block/needs_gold_tool.json',{'values':['minecraft:diamond_ore','minecraft:deepslate_diamond_ore']})
w(f'data/{NS}/tags/block/incorrect_for_gold_reinforced_iron_tool.json',{'values':['#minecraft:needs_diamond_tool']})
BR={'logs':['#minecraft:logs'],'stone_bricks':['minecraft:stone_bricks','minecraft:mossy_stone_bricks','minecraft:cracked_stone_bricks','minecraft:chiseled_stone_bricks'],'iron':['minecraft:iron_block'],'gold':['minecraft:gold_block'],'obsidian':['minecraft:obsidian','minecraft:crying_obsidian'],'diamond':['minecraft:diamond_block'],'netherite':['minecraft:netherite_block']}
for k,v in BR.items(): w(f'data/{NS}/tags/block/beacon_range/{k}.json',{'values':v})
# worldgen
for name,feat,c in [('iron','ore_iron',4),('gold','ore_gold_buried',2),('copper','ore_copper_small',4),('diamond','ore_diamond_small',4),('diamond_medium','ore_diamond_medium',1)]:
    w(f'data/{NS}/worldgen/placed_feature/deep_ore_{name}.json',{'feature':f'minecraft:{feat}','placement':[{'type':'minecraft:count','count':c},{'type':'minecraft:in_square'},{'type':'minecraft:height_range','height':{'type':'minecraft:uniform','min_inclusive':{'absolute':-64},'max_inclusive':{'absolute':-48}}},{'type':'minecraft:biome'}]})
w('harderoverhaulcraft.mixins.json',{'required':True,'package':'com.harderoverhaulcraft.mixin','compatibilityLevel':'JAVA_21','mixins':['BeaconBlockEntityMixin','ToolMixin'],'injectors':{'defaultRequire':1}})
w('fabric.mod.json',{'schemaVersion':1,'id':NS,'version':'${version}','name':'HarderOverhaulCraft','description':'Harder progression: nugget smelting, rock shards, Rock Shaper, upgraded tools and tiered beacons.','authors':['HarderOverhaulCraft'],'license':'CC0-1.0','icon':f'assets/{NS}/icon.png','environment':'*',
 'entrypoints':{'main':['com.harderoverhaulcraft.HarderOverhaulCraft'],'client':['com.harderoverhaulcraft.client.HarderOverhaulCraftClient'],'jei_mod_plugin':['com.harderoverhaulcraft.client.jei.HocJeiPlugin']},
 'mixins':[f'{NS}.mixins.json'],'depends':{'fabricloader':'>=0.19.5','minecraft':'~26.3','java':'>=25','fabric-api':'*'},'suggests':{'jei':'*'}})
