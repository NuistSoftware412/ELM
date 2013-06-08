function partition2(TrainingData_File,NumberofPart)
%Sample:partition('sat_test',10)

%Partition the TrainingData_File into N parts
train_data=load(TrainingData_File);
lines=size(train_data,1);
interval=fix(lines/NumberofPart);
for i=1:NumberofPart
    fid=fopen(cat(2,TrainingData_File,num2str(i)),'w+');
    temp_data=train_data((interval*(i-1)+1):interval*i,:);
    for j=1:interval 
         fprintf(fid,'%f ',temp_data(j,:));
         fprintf(fid,'\n');   
    end
    fclose(fid);
end


clear train_data lines interval temp_data;