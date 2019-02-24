##
## Auto Generated makefile by CodeLite IDE
## any manual changes will be erased      
##
## Debug
ProjectName            :=CodeEncryptor
ConfigurationName      :=Debug
WorkspacePath          :=C:/Data/Development/Robotics
ProjectPath            :=C:/Data/Development/Workspaces/General/CodeEncryptor/c++
IntermediateDirectory  :=./Debug
OutDir                 := $(IntermediateDirectory)
CurrentFileName        :=
CurrentFilePath        :=
CurrentFileFullPath    :=
User                   :=Sridaran
Date                   :=16/02/2019
CodeLitePath           :=C:/Install/CodeLite
LinkerName             :=C:/Install/MinGW/TDM-GCC/bin/g++.exe
SharedObjectLinkerName :=C:/Install/MinGW/TDM-GCC/bin/g++.exe -shared -fPIC
ObjectSuffix           :=.o
DependSuffix           :=.o.d
PreprocessSuffix       :=.i
DebugSwitch            :=-g 
IncludeSwitch          :=-I
LibrarySwitch          :=-l
OutputSwitch           :=-o 
LibraryPathSwitch      :=-L
PreprocessorSwitch     :=-D
SourceSwitch           :=-c 
OutputFile             :=$(IntermediateDirectory)/$(ProjectName)
Preprocessors          :=
ObjectSwitch           :=-o 
ArchiveOutputSwitch    := 
PreprocessOnlySwitch   :=-E
ObjectsFileList        :="CodeEncryptor.txt"
PCHCompileFlags        :=
MakeDirCommand         :=makedir
RcCmpOptions           := 
RcCompilerName         :=C:/Install/MinGW/TDM-GCC/bin/windres.exe
LinkOptions            :=  
IncludePath            :=  $(IncludeSwitch). $(IncludeSwitch). 
IncludePCH             := 
RcIncludePath          := 
Libs                   := 
ArLibs                 :=  
LibPath                := $(LibraryPathSwitch). 

##
## Common variables
## AR, CXX, CC, AS, CXXFLAGS and CFLAGS can be overriden using an environment variables
##
AR       := C:/Install/MinGW/TDM-GCC/bin/ar.exe rcu
CXX      := C:/Install/MinGW/TDM-GCC/bin/g++.exe
CC       := C:/Install/MinGW/TDM-GCC/bin/gcc.exe
CXXFLAGS :=  -g -O0 -Wall  -std=c++11 $(Preprocessors)
CFLAGS   :=  -g -O0 -Wall $(Preprocessors)
ASFLAGS  := 
AS       := C:/Install/MinGW/TDM-GCC/bin/as.exe


##
## User defined environment variables
##
CodeLiteDir:=C:\Install\CodeLite
Objects0=$(IntermediateDirectory)/Encryptor.cpp$(ObjectSuffix) 



Objects=$(Objects0) 

##
## Main Build Targets 
##
.PHONY: all clean PreBuild PrePreBuild PostBuild MakeIntermediateDirs
all: $(OutputFile)

$(OutputFile): $(IntermediateDirectory)/.d $(Objects) 
	@$(MakeDirCommand) $(@D)
	@echo "" > $(IntermediateDirectory)/.d
	@echo $(Objects0)  > $(ObjectsFileList)
	$(LinkerName) $(OutputSwitch)$(OutputFile) @$(ObjectsFileList) $(LibPath) $(Libs) $(LinkOptions)

MakeIntermediateDirs:
	@$(MakeDirCommand) "./Debug"


$(IntermediateDirectory)/.d:
	@$(MakeDirCommand) "./Debug"

PreBuild:


##
## Objects
##
$(IntermediateDirectory)/Encryptor.cpp$(ObjectSuffix): Encryptor.cpp $(IntermediateDirectory)/Encryptor.cpp$(DependSuffix)
	$(CXX) $(IncludePCH) $(SourceSwitch) "C:/Data/Development/Workspaces/General/CodeEncryptor/c++/Encryptor.cpp" $(CXXFLAGS) $(ObjectSwitch)$(IntermediateDirectory)/Encryptor.cpp$(ObjectSuffix) $(IncludePath)
$(IntermediateDirectory)/Encryptor.cpp$(DependSuffix): Encryptor.cpp
	@$(CXX) $(CXXFLAGS) $(IncludePCH) $(IncludePath) -MG -MP -MT$(IntermediateDirectory)/Encryptor.cpp$(ObjectSuffix) -MF$(IntermediateDirectory)/Encryptor.cpp$(DependSuffix) -MM Encryptor.cpp

$(IntermediateDirectory)/Encryptor.cpp$(PreprocessSuffix): Encryptor.cpp
	$(CXX) $(CXXFLAGS) $(IncludePCH) $(IncludePath) $(PreprocessOnlySwitch) $(OutputSwitch) $(IntermediateDirectory)/Encryptor.cpp$(PreprocessSuffix) Encryptor.cpp


-include $(IntermediateDirectory)/*$(DependSuffix)
##
## Clean
##
clean:
	$(RM) -r ./Debug/


