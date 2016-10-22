%Brian Craft | craft1.brian@gmail.com
%neural network csc578 project 2

function y = neural_network(nodeLayers, inputs, targets, batchSize, numEpochs, eta, test_validation, l2, activation_function, cost_function, momentum, softmax_ind)

  %Checklist
    %training,validation,test
    %calculate cost and accuracy for test, validation and training and store the results for ploting
    %better weight initialization
    %tanh, ReLU, softmax
    %early stopping
    %adaptive learning rate
    %mini batch shuffling
    %l2 regularization
    %momentum
    %quadratic cost, cross entropy


  %test_validation = [80,10,10] or array of the sizes of the training, validation and test datasets (percentage)
  %actiavtion_functions = ['tanh', 'sigmoid', 'relu'] relu is default
  %cost_function = ['quadratic_cost', 'cross_entropy', 'log_like']
  %softmax = boolean

  %create the training, test and validation
  [train_ind,val_ind,test_ind] = dividerand(length(inputs),test_validation(1),test_validation(2),test_validation(3));
  input_train = inputs(:,train_ind);
  input_val = inputs(:,val_ind);
  input_test = inputs(:,test_ind);
  target_train = targets(:,train_ind);
  target_val = targets(:,val_ind);
  target_test = targets(:,test_ind);
  
  %Return accuracy and cost on each data partition (test, training, validation) after each epoch and so plots can be made
  epoch_array = [];
  mse_train_array = [];
  mse_val_array = [];
  mse_test_array = [];
  
  accuracy_train_array = [];
  accuracy_val_array = [];
  accuracy_test_array = [];
  
  %{
  %Log Likelinhood
  Return the learned network
  %}

  %create a cell array that will store the weights and bias
  weights = {};
  weights_upate = {};
  bias = {};
  bias_update = {};
    
  %counters used to make the weights and bias matrices
  to_layer = 2;
  from_layer = 1;

  %better weight and bias initialization
  %initialize weights and bias using a stanard deivation of 1/sqrt(N)
  for layer = 2 : length(nodeLayers)
      weights{to_layer} = normrnd(0,1/sqrt(length(input_train)),nodeLayers(to_layer), nodeLayers(from_layer));
      bias{to_layer} = normrnd(0,1/sqrt(length(input_train)),nodeLayers(to_layer), 1);
      to_layer = 1 + to_layer;
      from_layer = 1 + from_layer;
  end

  %we will store the mini batches here
  batches = {};
  target_batches = {};
  counter = 1;
  counter_terminate = 0;

  %create mini batches
  for start_pos = 1 : batchSize : length(input_train)
      
      if length(input_train) - start_pos < batchSize
          mb = input_train(:, start_pos:end);
          batches{counter} = mb;
          target = target_train(:, start_pos:end);
          target_batches{counter} = target;
      else
          mb = input_train(:, start_pos: start_pos + batchSize - 1);
          batches{counter} = mb;
          target = target_train(:, start_pos: start_pos + batchSize - 1);
          target_batches{counter} = target;
          counter = counter + 1;
          
      end %end the if else
      
  end %end the input loop

  %begin running an epoch until the max number of epochs is reached or all cases are correctly classified
  for epoch = 1 : numEpochs
      
      %shuffle the mini batches
      %a random order of indicies is generated from 1 to the number of batches
      %this index order is then used to loop through the mini batches
      random = randperm(length(batches));
      counter_batches = 1;
      
      %begin looping through each mini batch
      for batch = 1 : length(batches) 

          %initialize and store cell arrays to store intermediate and activation values
          intermediate_z = {};
          activation = {};
          activation{1} = batches{random(counter_batches)};
          delta = {};

          %begin feedforward
          for layer = 2 : length(nodeLayers)
              
              intermediate_z{layer} = bsxfun(@plus,(weights{layer} * activation{layer - 1}), bias{layer});
              
              %apply softmax
              if softmax_ind == 1 && layer == length(nodeLayers)
                    activation{layer} = softmax(intermediate_z{layer});
              end

              %activation functions
              %based on what the user inputs, the activation function is applied
              if strcmp(activation_function, 'tanh') == 1
                  activation{layer} = tanh(intermediate_z{layer});
              elseif strcmp(activation_function, 'sigmoid') == 1
                  activation{layer} = logsig(intermediate_z{layer});
              else
                  activation{layer} = poslin(intermediate_z{layer}); 
              end

             
          end
              
          %calculate the error                   
          err = (activation{length(nodeLayers)} - target_batches{random(counter_batches)});
          
          %based on user input, the derivative of the activation function is applied
          if strcmp(activation_function, 'tanh') == 1
              sigprime = 1 - power(tanh((intermediate_z{length(nodeLayers)})),2 );
          elseif strcmp(activation_function, 'sigmoid') == 1
              sigprime = logsig(intermediate_z{length(nodeLayers)}) .* (1 - logsig(intermediate_z{length(nodeLayers)})); 
          else
              sigprime = poslin(intermediate_z{length(nodeLayers)});
          end
          
          delta{length(nodeLayers)} = err .* sigprime;

          %begin backpropogating the error for L - 1, L -2 .... 2
          for layer = (length(nodeLayers) - 1) : -1 : 2
              delta{layer} = (weights{layer + 1}.' * delta{layer + 1}) .* (logsig(intermediate_z{layer}) .* (1 - logsig(intermediate_z{layer})));
          end

          %update weights and biases
          for layer = length(nodeLayers) : -1 : 2

              %no momentum used on the first epoch
              if epoch == 1 & batch == 1
                w = weights{layer} - eta/length(batches{random(counter_batches)}) * delta{layer} * activation{layer - 1}.';
                weights{layer} = w;
                b = bias{layer} - eta/length(batches{random(counter_batches)}) * sum(delta{layer}, 2);
                bias{layer} = b;

                %add in the weight updates
                update_weights = eta/length(batches{random(counter_batches)}) * delta{layer} * activation{layer - 1}.'
                update_biases = eta/length(batches{random(counter_batches)}) * sum(delta{layer}, 2);
                weights_upate{layer} = update_weights;
                bias_update{layer} = update_biases;

              %if we are on a subsequent epoch, we will begin using momentum
              else
                w = weights{layer} - (eta/length(batches{random(counter_batches)}) * delta{layer} * activation{layer - 1}.') + momentum * weights_upate{layer};
                weights{layer} = w;
                b = bias{layer} - eta/length(batches{random(counter_batches)}) * sum(delta{layer}, 2) + momentum * bias_update{layer};
                bias{layer} = b;

                %reset the weight update
                update_weights = (eta/length(batches{random(counter_batches)}) * delta{layer} * activation{layer - 1}.');
                update_biases = eta/length(batches{random(counter_batches)}) * sum(delta{layer}, 2);
                weights_upate{layer} = update_weights;
                bias_update{layer} = update_biases;
              end

          end

      counter_batches = counter_batches + 1;
      
      end %end mini batch loop

      %calculate error for the training, test and validaiton sets after the epoch is complete
      final_activations = {};
      final_activations{1} = input_train;
      final_activations_val = {};
      final_activations_val{1} = input_val;
      final_activations_test = {};
      final_activations_test{1} = input_test;

      for layer = 2 : length(nodeLayers)

          z = bsxfun(@plus,(weights{layer} * final_activations{layer - 1}), bias{layer});
          z_val = bsxfun(@plus,(weights{layer} * final_activations_val{layer - 1}), bias{layer});
          z_test = bsxfun(@plus,(weights{layer} * final_activations_test{layer - 1}), bias{layer});
          
          if strcmp(activation_function, 'tanh') == 1
             final_activations{layer} = tanh(z);
             final_activations_val{layer} = tanh(z_val);  
             final_activations_test{layer} = tanh(z_test);        
          elseif strcmp(activation_function, 'sigmoid') == 1
             final_activations{layer} = logsig(z);
             final_activations_val{layer} = logsig(z_val);  
             final_activations_test{layer} = logsig(z_test);  
          else
             final_activations{layer} = poslin(z);
             final_activations_val{layer} = poslin(z_val);  
             final_activations_test{layer} = poslin(z_test);          
          end

      end
      
      %class matrix
      if size(target_train,1) > 1
          %training
          [it,vt] = max(target_train);
          [i,v] = max(final_activations{length(nodeLayers)});
          confusion_array = vt - v;
          correct = sum(confusion_array(:)==0);
          accuracy = correct/length(input_train);
          %validation
          [it_val,vt_val] = max(target_val);
          [i_val,v_val] = max(final_activations_val{length(nodeLayers)});
          confusion_array_val = vt_val - v_val;
          correct_val = sum(confusion_array_val(:)==0);
          accuracy_val = correct_val/length(input_val);
          %testing
          [it_test,vt_test] = max(target_test);
          [i_test,v_test] = max(final_activations_test{length(nodeLayers)});
          confusion_array_test = vt_test - v_test;
          correct_test = sum(confusion_array_test(:)==0);
          accuracy_test = correct_test/length(input_test);
          
      %binary classification
      else
          %training
          confusion_array = target_train - round(final_activations{length(nodeLayers)});
          correct = sum(confusion_array(:)==0);
          accuracy = correct/length(input_train);
          %validation
          confusion_array_val = target_val - round(final_activations_val{length(nodeLayers)});
          correct_val = sum(confusion_array_val(:)==0);
          accuracy_val = correct_val/length(input_val);
          %testing
          confusion_array_test = target_test - round(final_activations_test{length(nodeLayers)});
          correct_test = sum(confusion_array_test(:)==0);
          accuracy_test = correct/length(input_test);
      end

      %l2
      %calculate the sum of weights squared
      sum_weights = 0;
      for w_counter = 2 : length(weights)
          w_sq = weights{w_counter}.^2;
          sum_count = sum(sum(w_sq));
          sum_weights = sum_weights + sum_count;
      end
           
      %final regularization factors 
      l2_factor_train =  l2/(2*length(input_train)) * sum_weights;
      l2_factor_val =  l2/(2*length(input_val)) * sum_weights;
      l2_factor_test =  l2/(2*length(input_test)) * sum_weights;
       
      %calculate cost using either quadratic cost, cross entropy
      if strcmp(cost_function, 'quadratic_cost') == 1
              cost = (1/(2*(length(input_train))) * sum(sum((.5 * (target_train - final_activations{length(nodeLayers)}).^2)))) + l2_factor_train;
              cost_val = (1/(2*(length(input_val))) * sum(sum((.5 * (target_val - final_activations_val{length(nodeLayers)}).^2)))) + l2_factor_val;
              cost_test = (1/(2*(length(input_test))) * sum(sum((.5 * (target_test - final_activations_test{length(nodeLayers)}).^2)))) + l2_factor_test;            
      elseif strcmp(cost_function, 'cross_entropy') == 1
               cost = sum(crossentropy(target_train, final_activations{length(nodeLayers)})) + l2_factor_train;
               cost_val = sum(crossentropy(target_val, final_activations_val{length(nodeLayers)})) + l2_factor_val;
               cost_test = sum(crossentropy(target_test, final_activations_test{length(nodeLayers)}));
      else
              cost = sum(sum(-log((target_train - final_activations{length(nodeLayers)})))) + l2_factor_train;
              cost_val = sum(sum(-log((target_val - final_activations_val{length(nodeLayers)})))) + l2_factor_val;
              cost_test = sum(sum(-log((target_test - final_activations_test{length(nodeLayers)})))) + l2_factor_test;
      end
      
      %print the header if we are on the first epoch
      if epoch == 1
          disp(' ')
          fprintf('      |                      Train                          ||                         Test                      ||                     Validation               \n');
          fprintf('-----------------------------------------------------------------------------------------------\n');
          fprintf('Ep    |      Cost      |   Corr   |      Acc         ||      Cost      |   Corr   |      Acc         ||      Cost      |   Corr   |      Acc       \n');
          fprintf('----------------------------------------------------------------------------------------------- \n');
      end
      
      fprintf(' %d    | %f5  | %d/%d  |   %f   ||   %f5  |  %d/%d |  %f    ||  %f5  |  %d/%d  |  %f  \n', epoch, cost, correct, length(input_train), accuracy, cost_val, correct_val, length(input_val), accuracy_val, cost_test, correct_test, length(input_test), accuracy_test);

      
      %return cost to array for the training, validation and testing
      epoch_array(epoch) = [epoch];
      mse_train_array(epoch) = [cost];
      mse_val_array(epoch) = [cost_val];
      mse_test_array(epoch) = [cost_test];
      
      %return accuracy to array for training, validation and testing
      accuracy_train_array(epoch) = [accuracy];
      accuracy_val_array(epoch) = [accuracy_val];
      accuracy_test_array(epoch) = [accuracy_test];
      
      %if the cost on the val set increases after an epoch, then the counter increments, otherwise it resets to 0
      if length(mse_val_array) > 1 & .1 * mse_val_array(end) > mse_val_array(end) - mse_val_array(end-1)
          counter_terminate = counter_terminate + 1;
      else
          counter_termiante = 0;
      
      %early stopping criteria check
      %termiante if all cases are classified correctly
      if correct == length(input_train)
          y = 'All cases were classified correctly';
          return;
      %if the cost has gone up for more then 5 epochs in a row, the function will terminate
      elseif counter_terminate > 5
          y = 'Validation cost is increasing';
          return;
      %continue if there is no early stopping criteria met
      else
          continue
      end
         
      %adaptive learning rate
      %if it's after 100 epochs and the mse increased the learning rate will be divide by 10
      %the learning rate cannot get lower then .0001
      if length(mse_val_array) > 100 && mse_val_array(end) > mse_val_array(end-1) && eta > .0001
          eta = eta * .1;
      end

  end %end epoch loop if needed

  y = 'Epochs reached';

end %end function